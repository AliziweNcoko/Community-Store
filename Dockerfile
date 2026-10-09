# Multi-stage Build Stage for React and Vue
FROM node:20-alpine AS builder
WORKDIR /app

# 1. Build React Web App
COPY web/package*.json ./web/
RUN cd web && npm install
COPY web/ ./web/
RUN cd web && npm run build

# 2. Build Vue 3 Web App
COPY vue/package*.json ./vue/
RUN cd vue && npm install
COPY vue/ ./vue/
RUN cd vue && npm run build

# Production Runner Stage
FROM node:20-alpine
WORKDIR /app

RUN npm install express

# Copy built assets
COPY --from=builder /app/web/dist ./dist-react
COPY --from=builder /app/vue/dist ./dist-vue

# Create dual-engine server routing to React (/) and Vue (/vue)
RUN echo "const express = require('express'); \
const path = require('path'); \
const app = express(); \
const PORT = process.env.PORT || 10000; \
app.use('/vue', express.static(path.join(__dirname, 'dist-vue'))); \
app.get('/vue/*', (req, res) => res.sendFile(path.join(__dirname, 'dist-vue', 'index.html'))); \
app.use(express.static(path.join(__dirname, 'dist-react'))); \
app.get('*', (req, res) => res.sendFile(path.join(__dirname, 'dist-react', 'index.html'))); \
app.listen(PORT, '0.0.0.0', () => console.log('Community Store (React & Vue) running on port ' + PORT));" > server.js

ENV PORT=10000
EXPOSE 10000

CMD ["node", "server.js"]
