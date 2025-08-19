import os
import pollinations
from googletrans import Translator

def translate_to_english(text: str) -> str:
    """
    Translates the given text to English using googletrans.

    :param text: The text to translate
    :return: Translated text in English
    """
    try:
        translator = Translator()
        translation = translator.translate(text, src="ko", dest="en")
        return translation.text
    except Exception as e:
        return f"Error translating text: {e}"

def generate_image(prompt: str, index: int, output_dir: str) -> str:
    """
    Generates an AI image using the given prompt (translated to English) and saves it to the specified output directory.

    :param prompt: The prompt to generate the image (in Korean or any language)
    :param index: The index of the image (used for file naming)
    :param output_dir: The directory where the image will be saved
    :return: The path to the saved image or an error message
    """
    # Ensure the output directory exists
    os.makedirs(output_dir, exist_ok=True)
    
    # Translate the prompt to English
    translated_prompt = translate_to_english(prompt)
    
    # Define the image file path
    output_path = os.path.join(output_dir, f"image_{index + 1}.png")
    
    try:
        # Initialize the Pollinations Image Model
        image_model = pollinations.Image(
            model=pollinations.Image.flux(),  # Use the "flux" model
            seed="random",
            width=1024,
            height=1024,
            enhance=False,
            nologo=True,
            private=True,
            safe=False,
            referrer="pollinations.py"
        )

        # Generate the image
        image = image_model(prompt=translated_prompt)

        # Save the image to the output path
        image.save(file=output_path)

        return output_path

    except Exception as e:
        return f"Error generating image: {e}"
