# Script to run the Spring Boot service without running tests
Write-Host "Starting Spring Boot service without tests..." -ForegroundColor Green

# Clean and compile the project (skip tests)
Write-Host "`n1. Cleaning and compiling project (skipping tests)..." -ForegroundColor Yellow
mvn clean compile -DskipTests

if ($LASTEXITCODE -ne 0) {
    Write-Host "Compilation failed!" -ForegroundColor Red
    exit 1
}

Write-Host "Compilation successful!" -ForegroundColor Green

# Run the Spring Boot application (skip tests)
Write-Host "`n2. Starting Spring Boot application..." -ForegroundColor Yellow
Write-Host "The service will:" -ForegroundColor Cyan
Write-Host "- Initialize database tables" -ForegroundColor Cyan
Write-Host "- Insert default database entries" -ForegroundColor Cyan
Write-Host "- Start log file processing in background thread" -ForegroundColor Cyan
Write-Host "- Read logsql.log and insert into DATABASE_LOG table" -ForegroundColor Cyan
Write-Host "- Set finish_analys = true when complete" -ForegroundColor Cyan

Write-Host "`nStarting application..." -ForegroundColor Green
mvn spring-boot:run -DskipTests
