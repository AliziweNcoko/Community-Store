# Deploying Community Store to Render

This project includes both:
1. **The Native Android App** (runs in the AI Studio streaming emulator, distributable as `.apk`).
2. **The Full Web Application** (located in `/web`), configured for 1-click deployment on **Render**, Vercel, or Netlify.

---

## 🚀 How to Deploy on Render in 3 Steps:

### Method 1: Connecting your GitHub Repository (Recommended)
1. Push this project to your **GitHub** account (use the Git / GitHub button in AI Studio).
2. Go to [https://dashboard.render.com](https://dashboard.render.com) and click **New +** ➔ **Static Site** (or **Blueprint**).
3. Connect your repository:
   - **Root Directory:** `web`
   - **Build Command:** `npm install && npm run build`
   - **Publish Directory:** `dist`
4. Click **Create Static Site**.
   Render will build and deploy your live web application at `https://community-store-web.onrender.com` with free SSL!

---

### Method 2: Automatic Deploy with `render.yaml`
Because this repository already contains the root `render.yaml` blueprint:
1. In Render Dashboard, click **New +** ➔ **Blueprint**.
2. Select your repository.
3. Render automatically reads `render.yaml` and deploys the web application with zero manual configuration.

---

### 💻 Running Locally (Desktop Web):
```bash
cd web
npm install
npm run dev
```
Open [http://localhost:3000](http://localhost:3000) in your browser.
