# Test script for register endpoint without phone number validation
Write-Host "Testing Register Endpoint without Phone Number Validation..." -ForegroundColor Green

# Test 1: Valid request without phone number
Write-Host "`n1. Testing with VALID data (no phone number):" -ForegroundColor Yellow
try {
    $body = @{
        username = "testuser$(Get-Random)"
        password = "password123"
        email = "test$(Get-Random)@example.com"
    } | ConvertTo-Json

    Write-Host "Request body: $body"
    
    $response = Invoke-WebRequest -Uri "http://localhost:8080/sqlanalys/register" -Method POST -ContentType "application/json" -Body $body
    Write-Host "SUCCESS! Status: $($response.StatusCode)" -ForegroundColor Green
    Write-Host "Content: $($response.Content)"
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
    if ($_.Exception.Response) {
        $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $responseBody = $reader.ReadToEnd()
        Write-Host "Response Body: $responseBody" -ForegroundColor Red
        Write-Host "Status Code: $($_.Exception.Response.StatusCode)" -ForegroundColor Red
    }
}

# Test 2: Valid request with phone number
Write-Host "`n2. Testing with VALID data (with phone number):" -ForegroundColor Yellow
try {
    $body = @{
        username = "testuser$(Get-Random)"
        password = "password123"
        email = "test$(Get-Random)@example.com"
        phoneNumber = "0123456789"
    } | ConvertTo-Json

    Write-Host "Request body: $body"
    
    $response = Invoke-WebRequest -Uri "http://localhost:8080/sqlanalys/register" -Method POST -ContentType "application/json" -Body $body
    Write-Host "SUCCESS! Status: $($response.StatusCode)" -ForegroundColor Green
    Write-Host "Content: $($response.Content)"
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
    if ($_.Exception.Response) {
        $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $responseBody = $reader.ReadToEnd()
        Write-Host "Response Body: $responseBody" -ForegroundColor Red
        Write-Host "Status Code: $($_.Exception.Response.StatusCode)" -ForegroundColor Red
    }
}

# Test 3: Valid request with empty phone number
Write-Host "`n3. Testing with VALID data (empty phone number):" -ForegroundColor Yellow
try {
    $body = @{
        username = "testuser$(Get-Random)"
        password = "password123"
        email = "test$(Get-Random)@example.com"
        phoneNumber = ""
    } | ConvertTo-Json

    Write-Host "Request body: $body"
    
    $response = Invoke-WebRequest -Uri "http://localhost:8080/sqlanalys/register" -Method POST -ContentType "application/json" -Body $body
    Write-Host "SUCCESS! Status: $($response.StatusCode)" -ForegroundColor Green
    Write-Host "Content: $($response.Content)"
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
    if ($_.Exception.Response) {
        $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $responseBody = $reader.ReadToEnd()
        Write-Host "Response Body: $responseBody" -ForegroundColor Red
        Write-Host "Status Code: $($_.Exception.Response.StatusCode)" -ForegroundColor Red
    }
}

Write-Host "`nPhone number validation removal test completed!" -ForegroundColor Green
