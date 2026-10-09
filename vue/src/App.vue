<template>
  <!-- Student Email Login View -->
  <div v-if="!user" style="min-height: 100vh; background: linear-gradient(135deg, #0f172a 0%, #004d30 100%); display: flex; align-items: center; justify-content: center; padding: 20px;">
    <div style="background: #ffffff; border-radius: 24px; width: 100%; maxWidth: 480px; padding: 36px; box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.25);">
      <div style="text-align: center; margin-bottom: 24px;">
        <div style="width: 64px; height: 64px; background: #007A4D; color: #fff; border-radius: 18px; display: inline-flex; align-items: center; justify-content: center; font-size: 30px; margin-bottom: 14px;">
          🎓
        </div>
        <h1 style="font-size: 24px; font-weight: 800; color: #0f172a; margin-bottom: 6px;">Student Email Login (Vue 3)</h1>
        <p style="font-size: 13px; color: #64748b;">Community Store • South African University Marketplace</p>
      </div>

      <div style="background: #e6f4ea; border: 1px solid #bbf7d0; border-radius: 12px; padding: 12px; margin-bottom: 20px; display: flex; gap: 10px;">
        <span style="font-size: 18px;">🛡️</span>
        <div style="font-size: 12px; color: #166534; line-height: 1.4;">
          <strong>Verified .ac.za Domain SSO:</strong> No phone numbers or SMS OTP required. Authenticate directly with your university student email.
        </div>
      </div>

      <form @submit.prevent="handleLogin" style="display: flex; flex-direction: column; gap: 14px;">
        <div>
          <label style="display: block; font-size: 12px; font-weight: 600; color: #475569; margin-bottom: 6px;">Quick Select University Domain:</label>
          <div style="display: flex; flex-wrap: wrap; gap: 6px;">
            <button
              v-for="u in UNIVERSITIES"
              :key="u.id"
              type="button"
              @click="handleDomainSelect(u.domain)"
              style="background: #f1f5f9; border: 1px solid #cbd5e1; border-radius: 8px; padding: 6px 10px; font-size: 11px; cursor: pointer; font-weight: 600;"
            >
              {{ u.id }} ({{ u.domain }})
            </button>
          </div>
        </div>

        <div>
          <label style="display: block; font-size: 12px; font-weight: 600; color: #475569; margin-bottom: 6px;">Student University Email (.ac.za)</label>
          <input
            type="email"
            required
            v-model="loginEmail"
            placeholder="e.g. n.khumalo@myuct.ac.za"
            style="width: 100%; padding: 12px 14px; border-radius: 10px; border: 1px solid #cbd5e1; font-size: 14px;"
          />
        </div>

        <div>
          <label style="display: block; font-size: 12px; font-weight: 600; color: #475569; margin-bottom: 6px;">Full Name</label>
          <input
            type="text"
            v-model="loginName"
            placeholder="e.g. Nandi Khumalo"
            style="width: 100%; padding: 12px 14px; border-radius: 10px; border: 1px solid #cbd5e1; font-size: 14px;"
          />
        </div>

        <div>
          <label style="display: block; font-size: 12px; font-weight: 600; color: #475569; margin-bottom: 6px;">Student Number</label>
          <input
            type="text"
            v-model="loginId"
            placeholder="e.g. KHMNDI004"
            style="width: 100%; padding: 12px 14px; border-radius: 10px; border: 1px solid #cbd5e1; font-size: 14px;"
          />
        </div>

        <div>
          <label style="display: block; font-size: 12px; font-weight: 600; color: #475569; margin-bottom: 6px;">Student Password</label>
          <input
            type="password"
            required
            v-model="loginPassword"
            style="width: 100%; padding: 12px 14px; border-radius: 10px; border: 1px solid #cbd5e1; font-size: 14px;"
          />
        </div>

        <button
          type="submit"
          style="width: 100%; padding: 14px; background: #007A4D; color: #ffffff; border: none; border-radius: 12px; font-size: 15px; font-weight: 700; cursor: pointer; margin-top: 6px;"
        >
          Sign In with Student Email
        </button>
      </form>
    </div>
  </div>

  <!-- Authenticated Application View -->
  <div v-else style="min-height: 100vh; background: #f8faf9; display: flex; flex-direction: column;">
    <!-- Top Navbar -->
    <header style="background: #ffffff; border-bottom: 1px solid #e2e8f0; position: sticky; top: 0; z-index: 100;">
      <div style="max-width: 1240px; margin: 0 auto; padding: 12px 24px; display: flex; align-items: center; justify-content: space-between; gap: 20px;">
        <div style="display: flex; align-items: center; gap: 12px; cursor: pointer;" @click="activeTab = 'store'">
          <div style="width: 40px; height: 40px; background: #007A4D; color: #fff; border-radius: 10px; display: flex; align-items: center; justify-content: center; font-size: 20px;">
            🇿🇦
          </div>
          <div>
            <div style="font-weight: 800; font-size: 17px; color: #0f172a;">Community Store <span style="font-size: 11px; background: #e0f2fe; color: #0369a1; padding: 2px 6px; border-radius: 4px;">Vue 3</span></div>
            <div style="font-size: 11px; color: #059669; font-weight: 600;">Campus & Local Marketplace</div>
          </div>
        </div>

        <div style="display: flex; gap: 8px;">
          <button
            v-for="t in tabs"
            :key="t.id"
            @click="activeTab = t.id"
            :style="{
              background: activeTab === t.id ? '#007A4D' : 'transparent',
              color: activeTab === t.id ? '#fff' : '#475569',
              border: 'none',
              padding: '8px 16px',
              borderRadius: '8px',
              fontWeight: '600',
              cursor: 'pointer',
              fontSize: '13px'
            }"
          >
            {{ t.label }} {{ t.id === 'orders' ? `(${orders.length})` : '' }}
          </button>
          <a
            href="/"
            style="background: #0284c7; color: #ffffff; text-decoration: none; padding: 8px 14px; border-radius: 8px; font-weight: 700; font-size: 12px; display: flex; align-items: center; gap: 4px;"
          >
            Switch to React ➔
          </a>
        </div>

        <div style="display: flex; align-items: center; gap: 16px;">
          <button
            @click="isCartOpen = true"
            style="background: #f1f5f9; border: 1px solid #cbd5e1; padding: 8px 14px; border-radius: 10px; cursor: pointer; font-weight: 700; font-size: 13px; display: flex; align-items: center; gap: 6px;"
          >
            🛒 Cart <span style="background: #007A4D; color: #fff; border-radius: 10px; padding: 2px 8px; font-size: 11px;">{{ cart.length }}</span>
          </button>

          <div style="display: flex; align-items: center; gap: 8px; background: #f8fafc; padding: 6px 12px; border-radius: 10px; border: 1px solid #e2e8f0;">
            <div style="font-size: 12px;">
              <span style="font-weight: 700;">{{ user.name }}</span>
              <span style="display: block; font-size: 10px; color: #059669; font-weight: 600;">🎓 {{ user.email }}</span>
            </div>
            <button
              @click="user = null"
              style="background: transparent; border: none; color: #dc2626; cursor: pointer; font-size: 12px; font-weight: 600; margin-left: 6px;"
            >
              Sign Out
            </button>
          </div>
        </div>
      </div>
    </header>

    <!-- Main Container -->
    <main style="max-width: 1240px; margin: 0 auto; padding: 24px; flex: 1; width: 100%;">
      <!-- Banner -->
      <div v-if="activeTab === 'store'" style="background: linear-gradient(rgba(0,0,0,0.6), rgba(0,0,0,0.85)), url(https://images.unsplash.com/photo-1541339907198-e08756dedf3f?auto=format&fit=crop&w=1200&q=80); background-size: cover; background-position: center; color: #fff; border-radius: 20px; padding: 40px; margin-bottom: 24px;">
        <span style="background: #007A4D; padding: 4px 12px; border-radius: 6px; font-size: 12px; font-weight: 700;">🇿🇦 MZANSI CAMPUS STORE • VUE 3</span>
        <h2 style="font-size: 28px; font-weight: 800; margin-top: 12px; margin-bottom: 8px;">Trusted South African Student & Local Marketplace</h2>
        <p style="font-size: 14px; max-width: 640px; opacity: 0.9;">
          Exchange textbooks, electronics, dorm essentials, and local farm goods securely with verified university students and vendors.
        </p>
      </div>

      <!-- Filters -->
      <div v-if="activeTab === 'store'" style="display: flex; flex-wrap: wrap; gap: 12px; align-items: center; justify-content: space-between; margin-bottom: 24px; background: #fff; padding: 16px; border-radius: 16px; border: 1px solid #e2e8f0;">
        <input
          type="text"
          v-model="searchQuery"
          placeholder="Search textbooks, dorm tech, produce..."
          style="flex: 1; min-width: 240px; padding: 10px 14px; border-radius: 10px; border: 1px solid #cbd5e1; font-size: 14px;"
        />

        <select
          v-model="selectedCampus"
          style="padding: 10px 14px; border-radius: 10px; border: 1px solid #cbd5e1; font-size: 13px; font-weight: 600; background: #f8fafc;"
        >
          <option value="ALL">All SA Campuses</option>
          <option v-for="u in UNIVERSITIES" :key="u.id" :value="u.id">{{ u.name }}</option>
        </select>
      </div>

      <!-- Category Pills -->
      <div v-if="activeTab === 'store'" style="display: flex; flex-wrap: wrap; gap: 8px; margin-bottom: 24px;">
        <button
          v-for="cat in CATEGORIES"
          :key="cat"
          @click="selectedCategory = cat"
          :style="{
            background: selectedCategory === cat ? '#007A4D' : '#ffffff',
            color: selectedCategory === cat ? '#ffffff' : '#475569',
            border: '1px solid #e2e8f0',
            padding: '8px 16px',
            borderRadius: '20px',
            fontWeight: '600',
            fontSize: '12px',
            cursor: 'pointer'
          }"
        >
          {{ cat }}
        </button>
      </div>

      <!-- Products Grid -->
      <div v-if="activeTab === 'store'" style="display: grid; grid-template-columns: repeat(auto-fill, minmax(280px, 1fr)); gap: 20px;">
        <div v-for="p in filteredProducts" :key="p.id" style="background: #ffffff; border-radius: 16px; border: 1px solid #e2e8f0; overflow: hidden; display: flex; flex-direction: column;">
          <div style="height: 170px; position: relative;">
            <img :src="p.img" :alt="p.title" style="width: 100%; height: 100%; object-fit: cover;" />
            <span style="position: absolute; top: 10px; left: 10px; background: rgba(0,0,0,0.7); color: #fff; padding: 4px 8px; border-radius: 6px; font-size: 11px; font-weight: 700;">
              {{ p.campus }}
            </span>
            <span style="position: absolute; top: 10px; right: 10px; background: #dcfce7; color: #15803d; padding: 4px 8px; border-radius: 6px; font-size: 10px; font-weight: 700;">
              -{{ p.ecoKg }}kg CO₂
            </span>
          </div>

          <div style="padding: 16px; display: flex; flex-direction: column; flex: 1;">
            <div style="font-size: 11px; color: #64748b; font-weight: 600; margin-bottom: 4px;">{{ p.category }} • {{ p.condition }}</div>
            <h3 style="font-size: 15px; font-weight: 700; color: #0f172a; margin-bottom: 8px; line-height: 1.3;">{{ p.title }}</h3>
            <p style="font-size: 12px; color: #64748b; margin-bottom: 12px; flex: 1;">{{ p.desc }}</p>

            <div style="background: #fef3c7; padding: 8px 10px; border-radius: 8px; font-size: 11px; color: #92400e; margin-bottom: 14px;">
              📍 <strong>Safe Meetup:</strong> {{ p.safeSpot }}
            </div>

            <div style="display: flex; align-items: center; justify-content: space-between; border-top: 1px solid #f1f5f9; padding-top: 12px;">
              <div>
                <span style="font-size: 18px; font-weight: 800; color: #007A4D;">R {{ p.price.toFixed(2) }}</span>
                <span v-if="p.origPrice" style="font-size: 12px; color: #94a3b8; text-decoration: line-through; margin-left: 6px;">R {{ p.origPrice }}</span>
              </div>

              <button
                @click="addToCart(p)"
                style="background: #007A4D; color: #fff; border: none; padding: 8px 16px; border-radius: 8px; font-weight: 700; font-size: 12px; cursor: pointer;"
              >
                + Add to Cart
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- Orders View -->
      <div v-if="activeTab === 'orders'" style="background: #fff; border-radius: 16px; padding: 24px; border: 1px solid #e2e8f0;">
        <h2 style="font-size: 20px; font-weight: 800; margin-bottom: 8px;">My Orders & Campus Escrow Protection</h2>
        <div style="display: flex; flex-direction: column; gap: 14px; margin-top: 16px;">
          <div v-for="order in orders" :key="order.id" style="border: 1px solid #cbd5e1; border-radius: 12px; padding: 16px; display: flex; justify-content: space-between; align-items: center; background: #f8fafc;">
            <div>
              <span style="font-weight: 800; font-size: 14px;">Order #{{ order.id }}</span>
              <span style="font-size: 11px; background: #dcfce7; color: #15803d; padding: 2px 8px; border-radius: 6px; font-weight: 700; margin-left: 10px;">
                {{ order.status }}
              </span>
              <div style="font-size: 13px; color: #334155; margin-top: 6px;">Items: {{ order.items }}</div>
              <div style="font-size: 11px; color: #64748b; margin-top: 2px;">📍 Safe Spot: {{ order.safeSpot }} • {{ order.payment }}</div>
            </div>
            <div style="text-align: right;">
              <div style="font-size: 18px; font-weight: 800; color: #007A4D;">R {{ order.total.toFixed(2) }}</div>
            </div>
          </div>
        </div>
      </div>

      <!-- Bulletin View -->
      <div v-if="activeTab === 'bulletin'" style="background: #fff; border-radius: 16px; padding: 24px; border: 1px solid #e2e8f0;">
        <h2 style="font-size: 20px; font-weight: 800; margin-bottom: 8px;">Campus Bulletin Board (Vue 3)</h2>
        <p style="font-size: 13px; color: #64748b; margin-bottom: 20px;">Announcements, textbook swaps and student clubs.</p>
        <div style="border: 1px solid #e2e8f0; border-radius: 12px; padding: 18px;">
          <span style="background: #e0f2fe; color: #0369a1; padding: 4px 8px; border-radius: 6px; font-size: 11px; font-weight: 700;">UCT • ECO DRIVE</span>
          <h3 style="font-size: 16px; font-weight: 700; margin-top: 8px; margin-bottom: 6px;">Semester 2 Textbook Charity Drive</h3>
          <p style="font-size: 13px; color: #475569;">Bring 2, take 1, or donate to first-year bursary students. Free fair-trade coffee provided outside Leslie Social.</p>
        </div>
      </div>
    </main>

    <!-- Cart Drawer -->
    <div v-if="isCartOpen" style="position: fixed; top: 0; right: 0; bottom: 0; width: 380px; background: #fff; box-shadow: -10px 0 30px rgba(0,0,0,0.15); z-index: 1000; padding: 24px; display: flex; flex-direction: column;">
      <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
        <h3 style="font-size: 18px; font-weight: 800;">Cart ({{ cart.length }})</h3>
        <button @click="isCartOpen = false" style="background: transparent; border: none; font-size: 18px; cursor: pointer;">✕</button>
      </div>

      <div style="flex: 1; overflow-y: auto; display: flex; flex-direction: column; gap: 12px;">
        <div v-if="cart.length === 0" style="text-align: center; color: #94a3b8; margin-top: 40px;">Your cart is empty.</div>
        <div v-for="item in cart" :key="item.product.id" style="border-bottom: 1px solid #f1f5f9; padding-bottom: 10px;">
          <div style="font-weight: 700; font-size: 13px;">{{ item.product.title }}</div>
          <div style="display: flex; justify-content: space-between; margin-top: 4px; font-size: 12px;">
            <span>Qty: {{ item.quantity }}</span>
            <strong style="color: #007A4D;">R {{ (item.product.price * item.quantity).toFixed(2) }}</strong>
          </div>
        </div>
      </div>

      <div v-if="cart.length > 0" style="border-top: 1px solid #e2e8f0; padding-top: 16px;">
        <div style="display: flex; justify-content: space-between; font-size: 16px; font-weight: 800; margin-bottom: 14px;">
          <span>Total:</span>
          <span style="color: #007A4D;">R {{ cartTotal.toFixed(2) }}</span>
        </div>
        <button
          @click="handleCheckout('Campus Escrow (SnapScan QR)')"
          style="width: 100%; padding: 12px; background: #0284c7; color: #fff; border: none; border-radius: 10px; font-weight: 700; cursor: pointer; font-size: 13px;"
        >
          Checkout via SnapScan (Escrow)
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'

const CATEGORIES = [
  'All Items',
  'Textbooks & Notes',
  'Tech & Electronics',
  'Dorm Essentials',
  'Local Produce & Food',
  'Thrift & Fashion',
  'Campus Tutoring & Services'
]

const UNIVERSITIES = [
  { id: 'UCT', name: 'University of Cape Town (UCT)', domain: '@myuct.ac.za' },
  { id: 'WITS', name: 'Wits University (Johannesburg)', domain: '@students.wits.ac.za' },
  { id: 'STELLENBOSCH', name: 'Stellenbosch University (SU)', domain: '@sun.ac.za' },
  { id: 'UP', name: 'University of Pretoria (Tuks)', domain: '@tuks.co.za' },
  { id: 'UJ', name: 'University of Johannesburg (UJ)', domain: '@student.uj.ac.za' },
  { id: 'RHODES', name: 'Rhodes University (Makhanda)', domain: '@campus.ru.ac.za' }
]

const INITIAL_PRODUCTS = [
  {
    id: 'prod-1',
    title: 'Calculus & Linear Algebra 8th Ed + Past Papers',
    desc: 'Standard MAM1000W prescribed textbook with annotated summaries.',
    price: 450,
    origPrice: 980,
    category: 'Textbooks & Notes',
    campus: 'UCT',
    condition: 'Like New',
    seller: 'Thabo Mthembu',
    safeSpot: 'Chancellor Oppenheimer Library Foyer',
    ecoKg: 6.2,
    img: 'https://images.unsplash.com/photo-1497633762265-9d179a990aa6?auto=format&fit=crop&w=600&q=80'
  },
  {
    id: 'prod-2',
    title: 'South African Commercial Law 6th Edition (Juta)',
    desc: 'Essential textbook for CML1001F with exam questions.',
    price: 380,
    origPrice: 750,
    category: 'Textbooks & Notes',
    campus: 'WITS',
    condition: 'Good',
    seller: 'Sipho Ndlovu',
    safeSpot: 'Wartenweiler Library Security Desk',
    ecoKg: 5.8,
    img: 'https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?auto=format&fit=crop&w=600&q=80'
  },
  {
    id: 'prod-3',
    title: 'Noise-Cancelling Wireless Headphones & Desk Lamp',
    desc: 'Perfect for late night studying. 30hr battery.',
    price: 850,
    origPrice: 1600,
    category: 'Tech & Electronics',
    campus: 'STELLENBOSCH',
    condition: 'Like New',
    seller: 'Anika van der Merwe',
    safeSpot: 'Neelsie Student Centre Security Kiosk',
    ecoKg: 8.5,
    img: 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?auto=format&fit=crop&w=600&q=80'
  }
]

const user = ref<{ email: string; name: string } | null>(null)
const loginEmail = ref('n.khumalo@myuct.ac.za')
const loginName = ref('Nandi Khumalo')
const loginId = ref('KHMNDI004')
const loginPassword = ref('Student2026!')

const selectedCategory = ref('All Items')
const selectedCampus = ref('ALL')
const searchQuery = ref('')
const activeTab = ref('store')
const isCartOpen = ref(false)

const tabs = [
  { id: 'store', label: 'Marketplace' },
  { id: 'bulletin', label: 'Campus Bulletin' },
  { id: 'orders', label: 'Orders & Escrow' }
]

const cart = ref<{ product: any; quantity: number }[]>([])
const orders = ref<any[]>([
  {
    id: 'CS-ZA-9481',
    items: 'Calculus & Linear Algebra 8th Ed',
    total: 450,
    status: 'Escrow Protected - Ready for Meetup',
    safeSpot: 'Chancellor Oppenheimer Library Foyer',
    payment: 'SnapScan Escrow'
  }
])

const handleDomainSelect = (domain: string) => {
  const prefix = loginEmail.value.split('@')[0] || 'student'
  loginEmail.value = `${prefix}${domain}`
}

const handleLogin = () => {
  user.value = {
    email: loginEmail.value,
    name: loginName.value || 'Verified Student'
  }
}

const addToCart = (product: any) => {
  const existing = cart.value.find(item => item.product.id === product.id)
  if (existing) {
    existing.quantity++
  } else {
    cart.value.push({ product, quantity: 1 })
  }
  isCartOpen.value = true
}

const handleCheckout = (paymentMethod: string) => {
  const subtotal = cart.value.reduce((acc, item) => acc + item.product.price * item.quantity, 0)
  orders.value.unshift({
    id: `CS-ZA-${Math.floor(1000 + Math.random() * 9000)}`,
    items: cart.value.map(i => `${i.product.title} (x${i.quantity})`).join(', '),
    total: subtotal,
    status: 'Escrow Protected',
    safeSpot: 'Library Security Foyer',
    payment: paymentMethod
  })
  cart.value = []
  isCartOpen.value = false
  activeTab.value = 'orders'
}

const filteredProducts = computed(() => {
  return INITIAL_PRODUCTS.filter(p => {
    const matchesCategory = selectedCategory.value === 'All Items' || p.category === selectedCategory.value
    const matchesCampus = selectedCampus.value === 'ALL' || p.campus === selectedCampus.value
    const matchesSearch = !searchQuery.value || p.title.toLowerCase().includes(searchQuery.value.toLowerCase())
    return matchesCategory && matchesCampus && matchesSearch
  })
})

const cartTotal = computed(() => {
  return cart.value.reduce((acc, item) => acc + item.product.price * item.quantity, 0)
})
</script>
