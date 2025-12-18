package iakka.platform.domain.idea.service;

import iakka.platform.domain.idea.entity.Idea;
import iakka.platform.domain.idea.repository.IdeaRepository;
import iakka.platform.global.service.R2StorageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class IdeaService {

    private final IdeaRepository ideaRepository;
    private final ObjectMapper objectMapper;
    private final R2StorageService r2StorageService;

    public IdeaService(IdeaRepository ideaRepository, R2StorageService r2StorageService) {
        this.ideaRepository = ideaRepository;
        this.r2StorageService = r2StorageService;
        this.objectMapper = new ObjectMapper();
    }

    public List<Idea> getIdeasByUserId(String userId) {
        return ideaRepository.findByUserId(userId);
    }

    public Idea generateVideo(String userId, String title, List<String> script) {
        Idea idea = new Idea(userId, title, null);
        ideaRepository.save(idea);

        try {
            // Get the script directory path (relative to project root)
            String projectRoot = System.getProperty("user.dir");
            String scriptsDir = Paths.get(projectRoot, "scripts").toString();
            String scriptPath = Paths.get(scriptsDir, "generate_video.py").toString();
            
            // Convert script list to JSON string
            String scriptJson = objectMapper.writeValueAsString(script);
            
            // Build the process command
            ProcessBuilder processBuilder = new ProcessBuilder(
                "python",
                scriptPath,
                "--user_id", userId,
                "--title", title,
                "--script", scriptJson
            );
            
            // Set working directory to scripts directory so relative paths work
            processBuilder.directory(new File(scriptsDir));
            
            // Redirect error stream to output stream
            processBuilder.redirectErrorStream(true);
            
            // Start the process
            Process process = processBuilder.start();
            
            // Read the output (the file path will be printed to stdout)
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }
            
            // Wait for the process to complete
            int exitCode = process.waitFor();
            
            if (exitCode != 0) {
                throw new RuntimeException("Video generation failed with exit code: " + exitCode + 
                    "\nOutput: " + output.toString());
            }
            
            // Extract the file path from output (last non-empty line should be the absolute path)
            String outputStr = output.toString().trim();
            String[] lines = outputStr.split("\n");
            String videoFilePath = null;
            
            // Find the last non-empty line (should be the absolute path)
            for (int i = lines.length - 1; i >= 0; i--) {
                String line = lines[i].trim();
                if (!line.isEmpty() && (line.endsWith(".mp4") || new File(line).exists())) {
                    videoFilePath = line;
                    break;
                }
            }
            
            if (videoFilePath == null || !new File(videoFilePath).exists()) {
                throw new RuntimeException("Video file path not found in output: " + outputStr);
            }
            
            // Upload the video file to R2
            File videoFile = new File(videoFilePath);
            String r2Key = String.format("videos/%s/%s_%d.mp4", userId, userId, Instant.now().getEpochSecond());
            String r2Url = r2StorageService.uploadFile(videoFile, r2Key);
            
            // Save the R2 URL to the database
            idea.setFileId(r2Url);
            ideaRepository.save(idea);
            
            // Delete the local temporary file after successful upload
            if (videoFile.exists() && videoFile.delete()) {
                // File deleted successfully
            } else {
                // Log warning if file deletion fails, but don't fail the operation
                System.err.println("Warning: Failed to delete local video file: " + videoFilePath);
            }
            
            return idea;
            
        } catch (Exception e) {
            throw new RuntimeException("Error generating video: " + e.getMessage(), e);
        }
    }


    public boolean updateFileId(String userId, String title, String fileId) {
        List<Idea> ideas = ideaRepository.findByUserIdAndTitle(userId, title);
        boolean updated = false;

        for (Idea idea : ideas) {
            if (idea.getFileId() == null) { // fileId가 null인 경우에만 업데이트
                idea.setFileId(fileId);
                ideaRepository.save(idea);
                updated = true;
            }
        }
        return updated;
    }

    public boolean deleteIdeaById(Long ideaId) {
        Optional<Idea> idea = ideaRepository.findById(ideaId);
        if (idea.isPresent()) {
            ideaRepository.deleteById(ideaId);
            return true;
        }
        return false;
    }
}
