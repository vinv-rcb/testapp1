# Test script for SQL Analysis API
Write-Host "Testing SQL Analysis API..." -ForegroundColor Green

$baseUrl = "http://localhost:8080"

# First, get a valid token by logging in
Write-Host "`n1. Getting authentication token..." -ForegroundColor Yellow
try {
    $loginBody = @{
        username = "r_admin"
        password = "radmin123"
    } | ConvertTo-Json

    $loginResponse = Invoke-RestMethod -Uri "$baseUrl/sqlanalys/login" -Method POST -Body $loginBody -ContentType "application/json"
    $token = $loginResponse.data.token
    Write-Host "Token obtained successfully" -ForegroundColor Cyan
} catch {
    Write-Host "Error getting token: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

# Test 2: Get all databases
Write-Host "`n2. Testing GET /api/sqlanalys/database" -ForegroundColor Yellow
try {
    $headers = @{
        "Authorization" = "Bearer $token"
    }
    $response = Invoke-RestMethod -Uri "$baseUrl/api/sqlanalys/database" -Method GET -Headers $headers
    Write-Host "Status: $($response.status)" -ForegroundColor Cyan
    Write-Host "Error Code: $($response.errorCode)" -ForegroundColor Cyan
    Write-Host "Error Description: $($response.errorDesc)" -ForegroundColor Cyan
    Write-Host "Number of databases: $($response.data.Count)" -ForegroundColor Cyan
    foreach ($db in $response.data) {
        Write-Host "  - $($db.database_name): $($db.database_desc)" -ForegroundColor Cyan
    }
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 3: Get all logs
Write-Host "`n3. Testing GET /api/sqlanalys/log" -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/api/sqlanalys/log" -Method GET -Headers $headers
    Write-Host "Status: $($response.status)" -ForegroundColor Cyan
    Write-Host "Error Code: $($response.errorCode)" -ForegroundColor Cyan
    Write-Host "Error Description: $($response.errorDesc)" -ForegroundColor Cyan
    Write-Host "Number of log entries: $($response.data.Count)" -ForegroundColor Cyan
    if ($response.data.Count -gt 0) {
        Write-Host "First log entry:" -ForegroundColor Cyan
        Write-Host "  Database: $($response.data[0].database_name)" -ForegroundColor Cyan
        Write-Host "  SQL: $($response.data[0].sql)" -ForegroundColor Cyan
        Write-Host "  Exec Time: $($response.data[0].exec_time)ms" -ForegroundColor Cyan
        Write-Host "  Exec Count: $($response.data[0].exe_count)" -ForegroundColor Cyan
    }
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 4: Get logs for specific database (BIZ)
Write-Host "`n4. Testing GET /api/sqlanalys/log?database=BIZ" -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/api/sqlanalys/log?database=BIZ" -Method GET -Headers $headers
    Write-Host "Status: $($response.status)" -ForegroundColor Cyan
    Write-Host "Error Code: $($response.errorCode)" -ForegroundColor Cyan
    Write-Host "Error Description: $($response.errorDesc)" -ForegroundColor Cyan
    Write-Host "Number of BIZ log entries: $($response.data.Count)" -ForegroundColor Cyan
    foreach ($log in $response.data) {
        Write-Host "  - $($log.database_name): $($log.exec_time)ms, count: $($log.exe_count)" -ForegroundColor Cyan
    }
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 5: Test with invalid token
Write-Host "`n5. Testing with invalid token..." -ForegroundColor Yellow
try {
    $invalidHeaders = @{
        "Authorization" = "Bearer invalid_token"
    }
    $response = Invoke-RestMethod -Uri "$baseUrl/api/sqlanalys/log" -Method GET -Headers $invalidHeaders
    Write-Host "Status: $($response.status)" -ForegroundColor Cyan
    Write-Host "Error Code: $($response.errorCode)" -ForegroundColor Cyan
    Write-Host "Error Description: $($response.errorDesc)" -ForegroundColor Cyan
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 6: Test without token
Write-Host "`n6. Testing without token..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/api/sqlanalys/log" -Method GET
    Write-Host "Status: $($response.status)" -ForegroundColor Cyan
    Write-Host "Error Code: $($response.errorCode)" -ForegroundColor Cyan
    Write-Host "Error Description: $($response.errorDesc)" -ForegroundColor Cyan
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 7: Check if log processing is finished
Write-Host "`n7. Checking log processing status..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/api/logs/statistics" -Method GET
    Write-Host "Log Statistics:" -ForegroundColor Cyan
    Write-Host $response.data -ForegroundColor Cyan
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`nTest completed!" -ForegroundColor Green
