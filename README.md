# 🚚 Projeto Fleet Telemetry Service

Um sistema distribuído de telemetria de frota e rastreamento em tempo real em alta frequência. O projeto simula veículos cruzando rotas urbanas reais, processa pings de GPS via mensageria assíncrona, calcula cercas virtuais (*geofencing*) com Redis Geospatial e transmite atualizações ao vivo para um dashboard interativo via WebSockets.

---

## Tech Stack & Arquitetura

* **Backend:** Java 21 & Spring Boot 4.1.1 (Virtual Threads, WebSockets / STOMP)
* **Messaging Broker:** RabbitMQ (Ingestão de pings de GPS em alta vazão)
* **Geospatial & Cache:** Redis Geospatial (`GEOADD`, `GEORADIUS`) + Pub/Sub
* **Database:** PostgreSQL + PostGIS (Histórico de rotas e auditoria de alertas)
* **Front-end Dashboard:** Leaflet.js (Mapas), Chart.js, Tailwind CSS, Vanilla JS
* **Containerization:** Docker & Docker Compose

---

## Funcionalidades Chave

1. **Simulação de Trajetória Real:** Engine de simulação com rotas mapeadas via coordenadas de precisão (interpolação geográfica).
2. **Geofencing Dinâmico:** Detecção instantânea de entrada/saída de zonas de restrição usando estruturas de dados do Redis.
3. **Telemetria Crítica & Alertas:** Monitoramento contínuo de velocidade, nível de bateria, temperatura do motor e emissão de alertas ao vivo.
4. **Dashboard Zero-Polling:** Streaming de dados bi-direcional via WebSockets para máxima eficiência e baixíssima latência.

---

## Como Executar o Ambiente

### Pré-requisitos
- **Java 21+**
- **Maven 3.8+**
- **Docker & Docker Compose**

### Execução Passo a Passo

1. **Clonar o repositório:**
   ```
   git clone [https://github.com/SEU-USUARIO/fleet-telemetry.git](https://github.com/SEU-USUARIO/fleet-telemetry.git)
   cd fleet-telemetry
   ```
2. **Iniciar infraestrutura (Redis e PostgreSQL):**
   ```
   docker compose up -d
   ```
3. **Iniciar a aplicação Backend (Spring Boot):**
   ```
   mvn spring-boot:run
   ```
4. **Acessar o Dashboard:**
   Abra o navegador em http://localhost:8080/index.html
   
