# Test script to verify register API is whitelisted
Write-Host "Testing Register API Whitelist..."

# Wait for application to start
Write-Host "Waiting for application to start..."
Start-Sleep 20

# Test health endpoint first
Write-Host "`n1. Testing health endpoint:"
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/sqlanalys/health" -Method GET
    Write-Host "SUCCESS! Health check: $response"
} catch {
    Write-Host "Health check failed: $($_.Exception.Message)"
}

# Test registration endpoint
Write-Host "`n2. Testing registration endpoint (should work without login):"
try {
    $body = @{
        username = "testuser$(Get-Random)"
        password = "password123"
        email = "test$(Get-Random)@example.com"
        phoneNumber = "0123456789"
    } | ConvertTo-Json

    $response = Invoke-RestMethod -Uri "http://localhost:8080/sqlanalys/register" -Method POST -ContentType "application/json" -Body $body
    Write-Host "SUCCESS! Registration response: $($response | ConvertTo-Json)"
} catch {
    Write-Host "Registration failed: $($_.Exception.Message)"
    if ($_.Exception.Response) {
        $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $responseBody = $reader.ReadToEnd()
        Write-Host "Response Body: $responseBody"
    }
}

# Test duplicate username
Write-Host "`n3. Testing duplicate username (should return error):"
try {
    $body = @{
        username = "admin"
        password = "password123"
        email = "admin2@example.com"
        phoneNumber = "0987654321"
    } | ConvertTo-Json

    $response = Invoke-RestMethod -Uri "http://localhost:8080/sqlanalys/register" -Method POST -ContentType "application/json" -Body $body
    Write-Host "Response: $($response | ConvertTo-Json)"
} catch {
    Write-Host "Expected error: $($_.Exception.Message)"
}

Write-Host "`nTest completed!"
