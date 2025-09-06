# Script to monitor application logs in real-time
Write-Host "Monitoring application logs..." -ForegroundColor Green
Write-Host "Press Ctrl+C to stop monitoring" -ForegroundColor Yellow
Write-Host ""

# Check if logs directory exists
if (Test-Path "logs") {
    Write-Host "Found logs directory. Monitoring logs/application.log..." -ForegroundColor Cyan
    Get-Content -Path "logs/application.log" -Wait -Tail 20
} else {
    Write-Host "Logs directory not found. Monitoring console output instead..." -ForegroundColor Yellow
    Write-Host "Run the application first to see logs in console." -ForegroundColor Yellow
}
