# Test script to debug register endpoint validation issues
Write-Host "Testing Register Endpoint with Validation Debug..." -ForegroundColor Green

# Test 1: Valid request
Write-Host "`n1. Testing with VALID data:" -ForegroundColor Yellow
try {
    $validBody = @{
        username = "testuser$(Get-Random)"
        password = "password123"
        email = "test$(Get-Random)@example.com"
        phoneNumber = "0123456789"
    } | ConvertTo-Json

    Write-Host "Request body: $validBody"
    
    $response = Invoke-WebRequest -Uri "http://localhost:8080/sqlanalys/register" -Method POST -ContentType "application/json" -Body $validBody
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

# Test 2: Invalid email
Write-Host "`n2. Testing with INVALID email:" -ForegroundColor Yellow
try {
    $invalidEmailBody = @{
        username = "testuser$(Get-Random)"
        password = "password123"
        email = "invalid-email"
        phoneNumber = "0123456789"
    } | ConvertTo-Json

    Write-Host "Request body: $invalidEmailBody"
    
    $response = Invoke-WebRequest -Uri "http://localhost:8080/sqlanalys/register" -Method POST -ContentType "application/json" -Body $invalidEmailBody
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

# Test 3: Invalid phone number
Write-Host "`n3. Testing with INVALID phone number:" -ForegroundColor Yellow
try {
    $invalidPhoneBody = @{
        username = "testuser$(Get-Random)"
        password = "password123"
        email = "test$(Get-Random)@example.com"
        phoneNumber = "123"  # Too short
    } | ConvertTo-Json

    Write-Host "Request body: $invalidPhoneBody"
    
    $response = Invoke-WebRequest -Uri "http://localhost:8080/sqlanalys/register" -Method POST -ContentType "application/json" -Body $invalidPhoneBody
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

# Test 4: Missing required fields
Write-Host "`n4. Testing with MISSING required fields:" -ForegroundColor Yellow
try {
    $missingFieldsBody = @{
        username = "testuser$(Get-Random)"
        password = "password123"
        # email missing
        phoneNumber = "0123456789"
    } | ConvertTo-Json

    Write-Host "Request body: $missingFieldsBody"
    
    $response = Invoke-WebRequest -Uri "http://localhost:8080/sqlanalys/register" -Method POST -ContentType "application/json" -Body $missingFieldsBody
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

Write-Host "`nValidation test completed!" -ForegroundColor Green
