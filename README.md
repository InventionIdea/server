## InventionIdea

Monorepo for the platform backend and video generation services.

- **core-api**: Java Spring Boot REST API (Gradle)
- **video-api**: Python video/asset processing utilities

### Repository structure
- `core-api/` — Spring Boot service
- `video-api/` — Python service and scripts
- `.gitignore` — single root ignore covers both subprojects

### Quick start (Windows PowerShell)

#### core-api
```powershell
cd core-api
.\gradlew.bat test
.\gradlew.bat bootRun
```

#### video-api
```powershell
cd video-api
py -3 -m venv venv
venv\Scripts\Activate.ps1
pip install -r requirements.txt
python main.py
```

### Requirements
- Java 17+ (for `core-api`)
- Python 3.10+ (for `video-api`)

### Notes
- Place any local secrets (e.g., `.env`, API tokens) in your workspace and keep them untracked.
- Root `.gitignore` is configured to exclude common build artifacts for both projects.

### License
This project is licensed under the MIT License. See `LICENSE` for details.


