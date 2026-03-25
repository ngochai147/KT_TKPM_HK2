# Deployment Guide

## Production Deployment

### Prerequisites
- Linux/Windows Server
- Java 11+ JRE
- MariaDB 10.3+
- Node.js 14+ (for frontend build)
- Docker (optional, for containerization)

---

## Deployment Steps

### 1. Database Setup

```bash
# Connect to MariaDB
mysql -u root -p

# Create databases
source /path/to/schema.sql
source /path/to/sample-data.sql

# Create application user
CREATE USER 'app_user'@'localhost' IDENTIFIED BY 'app_password';
GRANT ALL PRIVILEGES ON user_db.* TO 'app_user'@'localhost';
GRANT ALL PRIVILEGES ON restaurant_db.* TO 'app_user'@'localhost';
GRANT ALL PRIVILEGES ON order_db.* TO 'app_user'@'localhost';
FLUSH PRIVILEGES;
```

### 2. Backend Deployment

#### Build JAR Files
```bash
mvn clean package -DskipTests
```

#### Create Application Directories
```bash
mkdir -p /opt/food-delivery/{user-service,restaurant-service,order-service}
```

#### Deploy User Service
```bash
cp user-service/target/user-service-1.0.0.jar /opt/food-delivery/user-service/
cp user-service/src/main/resources/application.yml /opt/food-delivery/user-service/

# Update application.yml with production credentials
vi /opt/food-delivery/user-service/application.yml
```

#### Create Systemd Service Files

**File: `/etc/systemd/system/user-service.service`**
```ini
[Unit]
Description=User Service
After=network.target

[Service]
Type=simple
WorkingDirectory=/opt/food-delivery/user-service
ExecStart=/usr/bin/java -Xmx512m -jar user-service-1.0.0.jar
Restart=on-failure
RestartSec=5
User=appuser

[Install]
WantedBy=multi-user.target
```

#### Start Services
```bash
sudo systemctl daemon-reload
sudo systemctl enable user-service
sudo systemctl start user-service

# Repeat for restaurant-service and order-service
```

### 3. Frontend Deployment

#### Build React App
```bash
cd frontend
npm install
npm run build
```

#### Deploy to Web Server

**Option A: Using Nginx**
```bash
# Install Nginx
sudo apt-get install nginx

# Copy build files
sudo cp -r build/* /var/www/html/

# Configure Nginx proxy
sudo vi /etc/nginx/sites-available/default
```

**Nginx Configuration:**
```nginx
server {
    listen 80;
    server_name your-domain.com;

    root /var/www/html;
    index index.html;

    # Frontend routing
    location / {
        try_files $uri $uri/ /index.html;
    }

    # API Proxies
    location /api/users {
        proxy_pass http://localhost:8081;
    }

    location /api/restaurants {
        proxy_pass http://localhost:8082;
    }

    location /api/dishes {
        proxy_pass http://localhost:8082;
    }

    location /api/orders {
        proxy_pass http://localhost:8083;
    }

    location /api/order-items {
        proxy_pass http://localhost:8083;
    }
}
```

**Option B: Using Apache**
```bash
# Install Apache
sudo apt-get install apache2

# Enable modules
sudo a2enmod rewrite
sudo a2enmod proxy
sudo a2enmod proxy_http

# Copy build files
sudo cp -r build/* /var/www/html/

# Configure .htaccess
cat > /var/www/html/.htaccess <<EOF
<IfModule mod_rewrite.c>
  RewriteEngine On
  RewriteBase /
  RewriteRule ^index\.html$ - [L]
  RewriteCond %{REQUEST_FILENAME} !-f
  RewriteCond %{REQUEST_FILENAME} !-d
  RewriteRule . /index.html [L]
</IfModule>
EOF
```

---

## Docker Deployment (Optional)

### Create Dockerfiles

**User Service: `user-service/Dockerfile`**
```dockerfile
FROM openjdk:11-jre-slim
WORKDIR /app
COPY target/user-service-1.0.0.jar .
EXPOSE 8081
CMD ["java", "-jar", "user-service-1.0.0.jar"]
```

### Docker Compose Configuration

**`docker-compose.yml`**
```yaml
version: '3.8'

services:
  mariadb:
    image: mariadb:10.6
    container_name: mariadb
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_USER: app_user
      MYSQL_PASSWORD: app_password
    ports:
      - "3306:3306"
    volumes:
      - ./database/schema.sql:/docker-entrypoint-initdb.d/01-schema.sql
      - ./database/sample-data.sql:/docker-entrypoint-initdb.d/02-data.sql
      - mariadb_data:/var/lib/mysql

  user-service:
    build: ./user-service
    ports:
      - "8081:8081"
    depends_on:
      - mariadb
    environment:
      SPRING_DATASOURCE_URL: jdbc:mariadb://mariadb:3306/user_db
      SPRING_DATASOURCE_USERNAME: app_user
      SPRING_DATASOURCE_PASSWORD: app_password

  restaurant-service:
    build: ./restaurant-service
    ports:
      - "8082:8082"
    depends_on:
      - mariadb
    environment:
      SPRING_DATASOURCE_URL: jdbc:mariadb://mariadb:3306/restaurant_db
      SPRING_DATASOURCE_USERNAME: app_user
      SPRING_DATASOURCE_PASSWORD: app_password

  order-service:
    build: ./order-service
    ports:
      - "8083:8083"
    depends_on:
      - mariadb
    environment:
      SPRING_DATASOURCE_URL: jdbc:mariadb://mariadb:3306/order_db
      SPRING_DATASOURCE_USERNAME: app_user
      SPRING_DATASOURCE_PASSWORD: app_password

  frontend:
    build: ./frontend
    ports:
      - "80:3000"
    depends_on:
      - user-service
      - restaurant-service
      - order-service

volumes:
  mariadb_data:
```

### Deploy with Docker Compose
```bash
docker-compose up -d
```

---

## Environment Configuration

### Update Application Properties

**User Service: `application.yml`**
```yaml
spring:
  datasource:
    url: jdbc:mariadb://db-host:3306/user_db
    username: app_user
    password: app_password
```

### SSL/TLS Configuration

**Nginx:**
```bash
sudo apt-get install certbot python3-certbot-nginx
sudo certbot certonly --nginx -d your-domain.com
```

**Update Nginx config:**
```nginx
server {
    listen 443 ssl;
    server_name your-domain.com;

    ssl_certificate /etc/letsencrypt/live/your-domain.com/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/your-domain.com/privkey.pem;

    # ... rest of config
}

# Redirect HTTP to HTTPS
server {
    listen 80;
    server_name your-domain.com;
    return 301 https://$server_name$request_uri;
}
```

---

## Monitoring

### Health Checks

Add health endpoint to Spring Boot services:
```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info
  endpoint:
    health:
      show-details: always
```

Access health: `http://service-host:port/actuator/health`

### Logs

**Check Service Logs:**
```bash
sudo journalctl -u user-service -f
sudo journalctl -u restaurant-service -f
sudo journalctl -u order-service -f
```

**Nginx Logs:**
```bash
tail -f /var/log/nginx/access.log
tail -f /var/log/nginx/error.log
```

### Database Backups

```bash
# Backup
mysqldump -u root -p --all-databases > backup.sql

# Restore
mysql -u root -p < backup.sql
```

---

## Performance Optimization

### Database Indexing
Indexes are already defined in schema.sql

### Connection Pool
Update `application.yml`:
```yaml
spring:
  datasource:
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
      connection-timeout: 20000
```

### Caching
```yaml
spring:
  cache:
    type: caffeine
    caffeine:
      spec: maximumSize=500,expireAfterWrite=600s
```

### Load Balancing

**Nginx Load Balancer:**
```nginx
upstream backend {
    server localhost:8081;
    server localhost:8082;
    server localhost:8083;
}

server {
    listen 80;
    location / {
        proxy_pass http://backend;
    }
}
```

---

## Troubleshooting

### Service Won't Start
```bash
# Check service status
sudo systemctl status user-service

# View logs
sudo journalctl -xe
```

### Database Connection Issues
```bash
# Test connection
mysql -h host -u app_user -p -D user_db
```

### Frontend Not Connecting to Backend
- Verify backend services are running
- Check CORS headers
- Verify API endpoints in apiService.js
- Check browser console for errors

---

## Security Checklist

- [ ] Change default MariaDB password
- [ ] Use SSL/TLS certificates
- [ ] Configure firewall rules
- [ ] Implement rate limiting
- [ ] Add authentication/authorization
- [ ] Validate all inputs
- [ ] Use environment variables for secrets
- [ ] Regular security updates
- [ ] Database backups
- [ ] Monitor access logs

---

## Support
For issues and questions, refer to README.md or contact your DevOps team.
