# Debug script for register endpoint
Write-Host "Testing Register Endpoint Debug..."

# Test with different approaches
Write-Host "`n1. Testing with Invoke-RestMethod:"
try {
    $body = @{
        username = "testuser$(Get-Random)"
        password = "password123"
        email = "test$(Get-Random)@example.com"
        phoneNumber = "0123456789"
    } | ConvertTo-Json

    $response = Invoke-RestMethod -Uri "http://localhost:8080/sqlanalys/register" -Method POST -ContentType "application/json" -Body $body
    Write-Host "SUCCESS! Response: $($response | ConvertTo-Json)"
} catch {
    Write-Host "Error: $($_.Exception.Message)"
    if ($_.Exception.Response) {
        $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $responseBody = $reader.ReadToEnd()
        Write-Host "Response Body: $responseBody"
        Write-Host "Status Code: $($_.Exception.Response.StatusCode)"
    }
}

Write-Host "`n2. Testing with Invoke-WebRequest:"
try {
    $body = @{
        username = "testuser$(Get-Random)"
        password = "password123"
        email = "test$(Get-Random)@example.com"
        phoneNumber = "0123456789"
    } | ConvertTo-Json

    $response = Invoke-WebRequest -Uri "http://localhost:8080/sqlanalys/register" -Method POST -ContentType "application/json" -Body $body
    Write-Host "SUCCESS! Status: $($response.StatusCode)"
    Write-Host "Content: $($response.Content)"
} catch {
    Write-Host "Error: $($_.Exception.Message)"
    if ($_.Exception.Response) {
        $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $responseBody = $reader.ReadToEnd()
        Write-Host "Response Body: $responseBody"
        Write-Host "Status Code: $($_.Exception.Response.StatusCode)"
    }
}

Write-Host "`n3. Testing health endpoint:"
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/sqlanalys/health" -Method GET
    Write-Host "SUCCESS! Health: $response"
} catch {
    Write-Host "Health check failed: $($_.Exception.Message)"
}

Write-Host "`nDebug completed!"
