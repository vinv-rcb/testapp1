# Test script for login logging
Write-Host "Testing Login Logging..."

# Test successful login
Write-Host "`n1. Testing successful login (should see INFO log):"
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/user/login" -Method POST -ContentType "application/json" -Body '{"username":"admin","password":"admin123"}'
    Write-Host "Login successful - Token received"
} catch {
    Write-Host "Error: $($_.Exception.Message)"
}

# Test failed login
Write-Host "`n2. Testing failed login (should see WARN log):"
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/user/login" -Method POST -ContentType "application/json" -Body '{"username":"admin","password":"wrongpassword"}'
    Write-Host "Login successful - Token received"
} catch {
    Write-Host "Expected error: $($_.Exception.Message)"
}

# Test non-existent user
Write-Host "`n3. Testing non-existent user (should see WARN log):"
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/user/login" -Method POST -ContentType "application/json" -Body '{"username":"nonexistent","password":"password123"}'
    Write-Host "Login successful - Token received"
} catch {
    Write-Host "Expected error: $($_.Exception.Message)"
}

Write-Host "`nCheck the application logs for the logging messages!"
