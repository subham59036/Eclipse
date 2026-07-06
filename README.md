# Eclipse

Eclipse is an event-driven project and task management platform, built as a full-stack
learning project. Teams create Projects, add Tasks with assignees, priorities, and
deadlines, receive real-time notifications, and view live analytics. Every state change
publishes a domain event that fans out through Redis Pub/Sub (live push) and Redis
Streams (audit log).

## Stack
- Backend: Java 25, Spring Boot 4.1.0, PostgreSQL 18.4, Redis 8.2
- Frontend: React 19.2.5, Vite 6
- Infrastructure: Docker, Docker Compose, Nginx
- CI/CD: GitHub Actions
- Observability: Prometheus 3.x, Grafana 12.x

## Status
Under active development as part of a 60-part learning series. See Part 1 onward for
the full build log.