# Test script for SQL log reading functionality
Write-Host "Testing SQL Log Reading API..." -ForegroundColor Green

$baseUrl = "http://localhost:8080/api/logs"

# Test 1: Get all log entries
Write-Host "`n1. Testing GET /api/logs/all" -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/all" -Method GET
    Write-Host "Status: $($response.status)" -ForegroundColor Cyan
    Write-Host "Number of entries: $($response.data.Count)" -ForegroundColor Cyan
    Write-Host "First entry: $($response.data[0] | ConvertTo-Json -Compress)" -ForegroundColor Cyan
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 2: Get log entries by account ID
Write-Host "`n2. Testing GET /api/logs/account/112" -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/account/112" -Method GET
    Write-Host "Status: $($response.status)" -ForegroundColor Cyan
    Write-Host "Number of entries for account 112: $($response.data.Count)" -ForegroundColor Cyan
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 3: Get slow queries (execution time > 300ms)
Write-Host "`n3. Testing GET /api/logs/slow-queries?thresholdMs=300" -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/slow-queries?thresholdMs=300" -Method GET
    Write-Host "Status: $($response.status)" -ForegroundColor Cyan
    Write-Host "Number of slow queries: $($response.data.Count)" -ForegroundColor Cyan
    foreach ($entry in $response.data) {
        Write-Host "  Account $($entry.account_id): $($entry.exec_time_ms)ms" -ForegroundColor Cyan
    }
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 4: Get log statistics
Write-Host "`n4. Testing GET /api/logs/statistics" -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/statistics" -Method GET
    Write-Host "Status: $($response.status)" -ForegroundColor Cyan
    Write-Host "Statistics:" -ForegroundColor Cyan
    Write-Host $response.data -ForegroundColor Cyan
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 5: Get filtered log entries
Write-Host "`n5. Testing GET /api/logs/filter?minExecTime=200&maxExecTime=400" -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/filter?minExecTime=200&maxExecTime=400" -Method GET
    Write-Host "Status: $($response.status)" -ForegroundColor Cyan
    Write-Host "Number of filtered entries: $($response.data.Count)" -ForegroundColor Cyan
    foreach ($entry in $response.data) {
        Write-Host "  Account $($entry.account_id): $($entry.exec_time_ms)ms" -ForegroundColor Cyan
    }
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`nTest completed!" -ForegroundColor Green
