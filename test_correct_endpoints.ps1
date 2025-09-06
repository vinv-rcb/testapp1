# Test script for correct API endpoints
Write-Host "Testing correct API endpoints..." -ForegroundColor Green

$baseUrl = "http://localhost:8080"

# Test 1: Get authentication token first
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

# Test 2: Test CORRECT endpoint - /sqlanalys/database
Write-Host "`n2. Testing CORRECT endpoint: /sqlanalys/database" -ForegroundColor Yellow
try {
    $headers = @{
        "Authorization" = "Bearer $token"
    }
    $response = Invoke-RestMethod -Uri "$baseUrl/sqlanalys/database" -Method GET -Headers $headers
    Write-Host "SUCCESS! Status: $($response.status)" -ForegroundColor Green
    Write-Host "Number of databases: $($response.data.Count)" -ForegroundColor Cyan
    foreach ($db in $response.data) {
        Write-Host "  - $($db.database_name): $($db.database_desc)" -ForegroundColor Cyan
    }
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 3: Test CORRECT endpoint - /sqlanalys/log
Write-Host "`n3. Testing CORRECT endpoint: /sqlanalys/log" -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/sqlanalys/log" -Method GET -Headers $headers
    Write-Host "SUCCESS! Status: $($response.status)" -ForegroundColor Green
    Write-Host "Number of log entries: $($response.data.Count)" -ForegroundColor Cyan
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 4: Test INCORRECT endpoint - /api/sqlanalys/database (should fail now)
Write-Host "`n4. Testing INCORRECT endpoint: /api/sqlanalys/database (should fail now)" -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/api/sqlanalys/database" -Method GET -Headers $headers
    Write-Host "Unexpected success: $($response)" -ForegroundColor Yellow
} catch {
    Write-Host "Expected error: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 5: Test verification endpoints (no token required)
Write-Host "`n5. Testing verification endpoints (no token required)..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/api/verification/status" -Method GET
    Write-Host "Status check: $($response.data)" -ForegroundColor Cyan
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n=== ENDPOINT SUMMARY ===" -ForegroundColor Green
Write-Host "CORRECT endpoints:" -ForegroundColor Green
Write-Host "  - GET $baseUrl/sqlanalys/database (with token)" -ForegroundColor Cyan
Write-Host "  - GET $baseUrl/sqlanalys/log (with token)" -ForegroundColor Cyan
Write-Host "  - GET $baseUrl/api/verification/status (no token)" -ForegroundColor Cyan
Write-Host "  - GET $baseUrl/api/verification/sample-logs (no token)" -ForegroundColor Cyan
Write-Host "  - GET $baseUrl/api/verification/count-by-database (no token)" -ForegroundColor Cyan
Write-Host "`nINCORRECT endpoints (will return 404):" -ForegroundColor Red
Write-Host "  - GET $baseUrl/api/sqlanalys/database" -ForegroundColor Red
Write-Host "  - GET $baseUrl/api/sqlanalys/log" -ForegroundColor Red
