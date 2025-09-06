# Test script for admin update user API
Write-Host "Testing Admin Update User API..." -ForegroundColor Green

# Test 1: Update user role and status
Write-Host "`n1. Testing POST /sqlanalys/admin/update (update role and status):" -ForegroundColor Yellow
try {
    $body = @{
        username = "testuser123"  # Use an existing username
        role = "R_USER"
        status = "ACTIVE"
    } | ConvertTo-Json

    Write-Host "Request body: $body"
    
    $response = Invoke-WebRequest -Uri "http://localhost:8080/sqlanalys/admin/update" -Method POST -ContentType "application/json" -Body $body
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

# Test 2: Update only role
Write-Host "`n2. Testing POST /sqlanalys/admin/update (update role only):" -ForegroundColor Yellow
try {
    $body = @{
        username = "testuser123"
        role = "R_ADMIN"
    } | ConvertTo-Json

    Write-Host "Request body: $body"
    
    $response = Invoke-WebRequest -Uri "http://localhost:8080/sqlanalys/admin/update" -Method POST -ContentType "application/json" -Body $body
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

# Test 3: Update only status
Write-Host "`n3. Testing POST /sqlanalys/admin/update (update status only):" -ForegroundColor Yellow
try {
    $body = @{
        username = "testuser123"
        status = "PENDING"
    } | ConvertTo-Json

    Write-Host "Request body: $body"
    
    $response = Invoke-WebRequest -Uri "http://localhost:8080/sqlanalys/admin/update" -Method POST -ContentType "application/json" -Body $body
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

# Test 4: Test with non-existent user
Write-Host "`n4. Testing POST /sqlanalys/admin/update (non-existent user):" -ForegroundColor Yellow
try {
    $body = @{
        username = "nonexistentuser"
        role = "R_USER"
        status = "ACTIVE"
    } | ConvertTo-Json

    Write-Host "Request body: $body"
    
    $response = Invoke-WebRequest -Uri "http://localhost:8080/sqlanalys/admin/update" -Method POST -ContentType "application/json" -Body $body
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

Write-Host "`nAdmin update user test completed!" -ForegroundColor Green
