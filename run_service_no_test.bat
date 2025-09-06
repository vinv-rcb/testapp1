@echo off
echo Starting Spring Boot service without tests...
echo.

echo 1. Cleaning and compiling project (skipping tests)...
call mvn clean compile -DskipTests

if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b 1
)

echo Compilation successful!
echo.

echo 2. Starting Spring Boot application...
echo The service will:
echo - Initialize database tables
echo - Insert default database entries  
echo - Start log file processing in background thread
echo - Read logsql.log and insert into DATABASE_LOG table
echo - Set finish_analys = true when complete
echo.

echo Starting application...
call mvn spring-boot:run -DskipTests

pause
