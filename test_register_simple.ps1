# Simple test for registration API
Write-Host "Testing Registration API (No Login Required)..."

# Wait for application to start
Write-Host "Waiting for application to start..."
Start-Sleep 15

# Test registration
Write-Host "`nTesting registration:"
try {
    $body = @{
        username = "newuser"
        password = "password123"
        email = "newuser@example.com"
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
    }
}

Write-Host "`nTest completed!"
