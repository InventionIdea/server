import os
import sys
import json
import time
import shutil
import argparse
import asyncio
from services.tts import generate_tts_files
from services.image import generate_image
from services.video import overlay_image_and_text, create_video_from_image_and_audio, merge_videos
from services.bgm import add_bgm_to_video

def main():
    # Get the directory where this script is located
    script_dir = os.path.dirname(os.path.abspath(__file__))
    resource_dir = os.path.join(script_dir, "resource")
    output_dir = os.path.join(script_dir, "output")
    
    parser = argparse.ArgumentParser(description='Generate video from script')
    parser.add_argument('--user_id', required=True, help='User ID')
    parser.add_argument('--title', required=True, help='Video title')
    parser.add_argument('--script', required=True, help='Script as JSON string or file path')
    
    args = parser.parse_args()
    
    # Parse script - can be JSON string or file path
    if os.path.exists(args.script):
        with open(args.script, 'r', encoding='utf-8') as f:
            script = json.load(f)
    else:
        try:
            script = json.loads(args.script)
        except json.JSONDecodeError:
            print(f"Error: Invalid JSON in --script argument", file=sys.stderr)
            sys.exit(1)
    
    if not isinstance(script, list):
        print(f"Error: Script must be a list of strings", file=sys.stderr)
        sys.exit(1)
    
    # Ensure output directory exists
    os.makedirs(output_dir, exist_ok=True)
    
    # Create a temporary directory for the user
    timestamp = int(time.time())
    temp_dir = os.path.join(output_dir, f"{args.user_id}_{timestamp}")
    os.makedirs(temp_dir, exist_ok=True)
    
    try:
        results = []
        video_files = []
        
        # Process each sentence in the script
        for index, sentence in enumerate(script):
            # Generate image for sentence
            image_file_path = generate_image(sentence, index, output_dir=temp_dir)
            
            # Create final layout image with overlay and text
            final_image_path = os.path.join(temp_dir, f"layout_{index + 1}.png")
            background_path = os.path.join(resource_dir, "background.png")
            overlay_image_and_text(
                background_path=background_path,
                overlay_path=image_file_path,
                output_path=final_image_path,
                text=sentence,
                title=args.title
            )
            
            # Generate TTS file
            tts_file_path = asyncio.run(generate_tts_files(sentence, index, output_dir=temp_dir))
            
            # Create video from image and audio
            video_file_path = os.path.join(temp_dir, f"video_{index + 1}.mp4")
            create_video_from_image_and_audio(
                final_image_path,
                tts_file_path,
                video_file_path,
                shorten_by=0.2
            )
            
            video_files.append(video_file_path)
            
            results.append({
                "original": sentence,
                "tts_file": tts_file_path,
                "image_file": final_image_path,
                "video_file": video_file_path
            })
        
        # Merge all videos
        final_video_path = os.path.join(temp_dir, "final_video.mp4")
        merge_videos(video_files, final_video_path, temp_dir)
        
        # Add BGM
        bgm_path = os.path.join(resource_dir, "bgm.mp3")
        final_video_with_bgm = os.path.join(temp_dir, f"{args.user_id}_{timestamp}.mp4")
        add_bgm_to_video(
            final_video_path,
            bgm_path,
            final_video_with_bgm,
            volume=0.1
        )
        
        # Move final video to output directory (outside temp_dir) so it persists after cleanup
        final_output_path = os.path.join(output_dir, f"{args.user_id}_{timestamp}.mp4")
        shutil.move(final_video_with_bgm, final_output_path)
        
        # Print the absolute path of the final video to stdout
        absolute_path = os.path.abspath(final_output_path)
        print(absolute_path)
        
    except Exception as e:
        print(f"Error generating video: {e}", file=sys.stderr)
        sys.exit(1)
    finally:
        # Cleanup temporary directory (intermediate files only, final video is moved out)
        if os.path.exists(temp_dir):
            shutil.rmtree(temp_dir)
            print(f"Temporary directory {temp_dir} deleted.", file=sys.stderr)

if __name__ == "__main__":
    main()

