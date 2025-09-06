# Test script for registration API
Write-Host "Testing Registration API..."

# Test successful registration
Write-Host "`n1. Testing successful registration:"
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/user/register" -Method POST -ContentType "application/json" -Body '{"username":"newuser","password":"password123","email":"newuser@example.com","phoneNumber":"0123456789"}'
    Write-Host "Response: $($response | ConvertTo-Json)"
} catch {
    Write-Host "Error: $($_.Exception.Message)"
}

# Test duplicate username
Write-Host "`n2. Testing duplicate username:"
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/user/register" -Method POST -ContentType "application/json" -Body '{"username":"admin","password":"password123","email":"admin2@example.com","phoneNumber":"0987654321"}'
    Write-Host "Response: $($response | ConvertTo-Json)"
} catch {
    Write-Host "Error: $($_.Exception.Message)"
}

# Test duplicate email
Write-Host "`n3. Testing duplicate email:"
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/user/register" -Method POST -ContentType "application/json" -Body '{"username":"newuser2","password":"password123","email":"admin@company.com","phoneNumber":"0111111111"}'
    Write-Host "Response: $($response | ConvertTo-Json)"
} catch {
    Write-Host "Error: $($_.Exception.Message)"
}

# Test invalid phone number
Write-Host "`n4. Testing invalid phone number:"
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/user/register" -Method POST -ContentType "application/json" -Body '{"username":"testuser","password":"password123","email":"test@example.com","phoneNumber":"123"}'
    Write-Host "Response: $($response | ConvertTo-Json)"
} catch {
    Write-Host "Error: $($_.Exception.Message)"
}
