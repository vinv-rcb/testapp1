# Test script for admin list users API
Write-Host "Testing Admin List Users API..." -ForegroundColor Green

# Test 1: List all users except R_ADMIN
Write-Host "`n1. Testing GET /sqlanalys/admin/list-user:" -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8080/sqlanalys/admin/list-user" -Method GET
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

Write-Host "`nAdmin list users test completed!" -ForegroundColor Green
