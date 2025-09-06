# Script to check if log processing is working correctly
Write-Host "Checking log processing status..." -ForegroundColor Green

$baseUrl = "http://localhost:8080"

# Check processing status
Write-Host "`n1. Checking log processing status..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/api/verification/status" -Method GET
    Write-Host "Status Response:" -ForegroundColor Cyan
    Write-Host $response.data -ForegroundColor Cyan
} catch {
    Write-Host "Error checking status: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host "Make sure the service is running on port 8080" -ForegroundColor Red
    exit 1
}

# Check sample logs
Write-Host "`n2. Checking sample log entries..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/api/verification/sample-logs" -Method GET
    Write-Host "Sample Log Entries:" -ForegroundColor Cyan
    Write-Host "Total entries found: $($response.data.Count)" -ForegroundColor Cyan
    
    if ($response.data.Count -gt 0) {
        Write-Host "`nFirst few entries:" -ForegroundColor Cyan
        for ($i = 0; $i -lt [Math]::Min(3, $response.data.Count); $i++) {
            $log = $response.data[$i]
            Write-Host "  Entry $($i + 1):" -ForegroundColor Cyan
            Write-Host "    Database: $($log.databaseName)" -ForegroundColor White
            Write-Host "    SQL: $($log.sql)" -ForegroundColor White
            Write-Host "    Exec Time: $($log.exeTime)ms" -ForegroundColor White
            Write-Host "    Exec Count: $($log.exeCount)" -ForegroundColor White
            Write-Host "    Created: $($log.createdAt)" -ForegroundColor White
            Write-Host ""
        }
    }
} catch {
    Write-Host "Error getting sample logs: $($_.Exception.Message)" -ForegroundColor Red
}

# Check count by database
Write-Host "`n3. Checking log count by database..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/api/verification/count-by-database" -Method GET
    Write-Host "Log Count by Database:" -ForegroundColor Cyan
    Write-Host $response.data -ForegroundColor Cyan
} catch {
    Write-Host "Error getting count by database: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`nVerification completed!" -ForegroundColor Green
Write-Host "If you see log entries above, the log processing is working correctly." -ForegroundColor Green
