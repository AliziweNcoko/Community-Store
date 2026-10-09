# Build stage
FROM node:20-alpine AS builder
WORKDIR /app

# Copy web files and build
COPY web/package*.json ./web/
RUN cd web && npm install

COPY web/ ./web/
RUN cd web && npm run build

# Production stage
FROM node:20-alpine
WORKDIR /app

# Install lightweight static server
RUN npm install express

# Copy built frontend assets
COPY --from=builder /app/web/dist ./dist

# Create simple server to listen on Render's dynamic PORT
RUN echo "const express = require('express'); \
const path = require('path'); \
const app = express(); \
const PORT = process.env.PORT || 10000; \
app.use(express.static(path.join(__dirname, 'dist'))); \
app.get('*', (req, res) => res.sendFile(path.join(__dirname, 'dist', 'index.html'))); \
app.listen(PORT, '0.0.0.0', () => console.log('Community Store web app running on port ' + PORT));" > server.js

ENV PORT=10000
EXPOSE 10000

CMD ["node", "server.js"]
