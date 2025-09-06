# Test script for sqlanalys registration API
Write-Host "Testing SQLAnalys Registration API..."

# Wait for application to start
Write-Host "Waiting for application to start..."
Start-Sleep 15

# Test successful registration
Write-Host "`n1. Testing successful registration:"
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/sqlanalys/register" -Method POST -ContentType "application/json" -Body '{"username":"testuser1","password":"password123","email":"testuser1@example.com","phoneNumber":"0123456789"}'
    Write-Host "Response: $($response | ConvertTo-Json)"
} catch {
    Write-Host "Error: $($_.Exception.Message)"
    if ($_.Exception.Response) {
        Write-Host "Status Code: $($_.Exception.Response.StatusCode)"
    }
}

# Test duplicate username
Write-Host "`n2. Testing duplicate username:"
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/sqlanalys/register" -Method POST -ContentType "application/json" -Body '{"username":"admin","password":"password123","email":"admin2@example.com","phoneNumber":"0987654321"}'
    Write-Host "Response: $($response | ConvertTo-Json)"
} catch {
    Write-Host "Error: $($_.Exception.Message)"
}

# Test invalid phone number
Write-Host "`n3. Testing invalid phone number:"
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/sqlanalys/register" -Method POST -ContentType "application/json" -Body '{"username":"testuser2","password":"password123","email":"test2@example.com","phoneNumber":"123"}'
    Write-Host "Response: $($response | ConvertTo-Json)"
} catch {
    Write-Host "Error: $($_.Exception.Message)"
}

Write-Host "`nTest completed!"
