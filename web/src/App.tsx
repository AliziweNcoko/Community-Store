import React, { useState } from 'react';

// Product Categories
const CATEGORIES = [
  'All Items',
  'Textbooks & Notes',
  'Tech & Electronics',
  'Dorm Essentials',
  'Local Produce & Food',
  'Thrift & Fashion',
  'Campus Tutoring & Services'
];

// Universities
const UNIVERSITIES = [
  { id: 'UCT', name: 'University of Cape Town (UCT)', domain: '@myuct.ac.za' },
  { id: 'WITS', name: 'Wits University (Johannesburg)', domain: '@students.wits.ac.za' },
  { id: 'STELLENBOSCH', name: 'Stellenbosch University (SU)', domain: '@sun.ac.za' },
  { id: 'UP', name: 'University of Pretoria (Tuks)', domain: '@tuks.co.za' },
  { id: 'UJ', name: 'University of Johannesburg (UJ)', domain: '@student.uj.ac.za' },
  { id: 'RHODES', name: 'Rhodes University (Makhanda)', domain: '@campus.ru.ac.za' }
];

// Sample Listings
const INITIAL_PRODUCTS = [
  {
    id: 'prod-1',
    title: 'Calculus & Linear Algebra 8th Ed + Past Papers',
    desc: 'Standard MAM1000W prescribed textbook with annotated summaries. Clean condition.',
    price: 450,
    origPrice: 980,
    category: 'Textbooks & Notes',
    campus: 'UCT',
    condition: 'Like New',
    seller: 'Thabo Mthembu',
    role: 'Verified Student',
    rating: 4.95,
    safeSpot: 'Chancellor Oppenheimer Library Foyer',
    ecoKg: 6.2,
    img: 'https://images.unsplash.com/photo-1497633762265-9d179a990aa6?auto=format&fit=crop&w=600&q=80'
  },
  {
    id: 'prod-2',
    title: 'South African Commercial Law 6th Edition (Juta)',
    desc: 'Essential textbook for CML1001F. Includes highlighted case law analysis and exam notes.',
    price: 380,
    origPrice: 750,
    category: 'Textbooks & Notes',
    campus: 'WITS',
    condition: 'Good',
    seller: 'Sipho Ndlovu',
    role: 'Verified Student',
    rating: 4.88,
    safeSpot: 'Wartenweiler Library Security Desk',
    ecoKg: 5.8,
    img: 'https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?auto=format&fit=crop&w=600&q=80'
  },
  {
    id: 'prod-3',
    title: 'Noise-Cancelling Wireless Headphones & Desk Lamp',
    desc: 'Perfect for dorm studying. 30hr battery, soft cushions, includes warm LED study light.',
    price: 850,
    origPrice: 1600,
    category: 'Tech & Electronics',
    campus: 'STELLENBOSCH',
    condition: 'Like New',
    seller: 'Anika van der Merwe',
    role: 'Verified Student',
    rating: 4.92,
    safeSpot: 'Neelsie Student Centre Security Kiosk',
    ecoKg: 8.5,
    img: 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?auto=format&fit=crop&w=600&q=80'
  },
  {
    id: 'prod-4',
    title: 'Dorm Study Bundle: 1.7L Stainless Kettle + Multi-Plug',
    desc: 'Residence essentials pack. Surge-protected multi-plug adapter and quick boil kettle.',
    price: 390,
    origPrice: 790,
    category: 'Dorm Essentials',
    campus: 'UP',
    condition: 'Good',
    seller: 'Kagiso Dlamini',
    role: 'Campus Resident',
    rating: 4.85,
    safeSpot: 'Hatfield Student Union Quad',
    ecoKg: 4.0,
    img: 'https://images.unsplash.com/photo-1583847268964-b28dc8f51f92?auto=format&fit=crop&w=600&q=80'
  },
  {
    id: 'prod-5',
    title: 'Organic Student Veggie Box & Artisanal Sourdough',
    desc: 'Fresh farm produce: avocados, baby spinach, heirloom tomatoes, and sourdough loaf.',
    price: 180,
    origPrice: 260,
    category: 'Local Produce & Food',
    campus: 'UCT',
    condition: 'Fresh / New',
    seller: 'Fynbos Community Farm',
    role: 'Verified Vendor (CIPC)',
    rating: 4.98,
    safeSpot: 'Leslie Social Science Plaza Stalls',
    ecoKg: 12.0,
    img: 'https://images.unsplash.com/photo-1610348725531-843dff563e2c?auto=format&fit=crop&w=600&q=80'
  },
  {
    id: 'prod-6',
    title: 'Python, Java & Data Structures 1-on-1 Tutoring (2 hrs)',
    desc: 'Honours Computer Science student. Help with coding assignments, algorithms and exams.',
    price: 250,
    origPrice: 400,
    category: 'Campus Tutoring & Services',
    campus: 'UCT',
    condition: 'Digital Service',
    seller: 'Brandon Pillay (Dean List)',
    role: 'Verified Student',
    rating: 4.96,
    safeSpot: 'Upper Campus Computer Science Lab',
    ecoKg: 1.0,
    img: 'https://images.unsplash.com/photo-1517694712202-14dd9538aa97?auto=format&fit=crop&w=600&q=80'
  }
];

export default function App() {
  // Auth state
  const [user, setUser] = useState<{ email: string; name: string; campus: string; studentId: string } | null>(null);
  const [loginEmail, setLoginEmail] = useState('n.khumalo@myuct.ac.za');
  const [loginPassword, setLoginPassword] = useState('Student2026!');
  const [loginName, setLoginName] = useState('Nandi Khumalo');
  const [loginId, setLoginId] = useState('KHMNDI004');

  // App state
  const [selectedCategory, setSelectedCategory] = useState('All Items');
  const [selectedCampus, setSelectedCampus] = useState('ALL');
  const [searchQuery, setSearchQuery] = useState('');
  const [cart, setCart] = useState<{ product: any; quantity: number }[]>([]);
  const [orders, setOrders] = useState<any[]>([
    {
      id: 'CS-ZA-9481',
      date: 'Yesterday',
      items: 'Calculus & Linear Algebra 8th Ed',
      total: 450,
      payment: 'Campus Escrow (SnapScan)',
      status: 'Escrow Protected - Ready for Meetup',
      safeSpot: 'Chancellor Oppenheimer Library Foyer'
    }
  ]);

  const [activeTab, setActiveTab] = useState<'store' | 'bulletin' | 'orders' | 'proposal'>('store');
  const [isCartOpen, setIsCartOpen] = useState(false);
  const [orderNotification, setOrderNotification] = useState('');

  // Auto-detect campus from email
  const handleDomainSelect = (domain: string, campusId: string) => {
    const prefix = loginEmail.split('@')[0] || 'student';
    setLoginEmail(`${prefix}${domain}`);
  };

  const handleLogin = (e: React.FormEvent) => {
    e.preventDefault();
    if (!loginEmail) return;
    setUser({
      email: loginEmail,
      name: loginName || 'Verified Student',
      campus: loginEmail.includes('wits') ? 'WITS' : loginEmail.includes('sun') ? 'STELLENBOSCH' : 'UCT',
      studentId: loginId || 'STU-1004'
    });
  };

  const addToCart = (product: any) => {
    setCart(prev => {
      const exists = prev.find(item => item.product.id === product.id);
      if (exists) {
        return prev.map(item => item.product.id === product.id ? { ...item, quantity: item.quantity + 1 } : item);
      }
      return [...prev, { product, quantity: 1 }];
    });
    setOrderNotification(`Added "${product.title.slice(0, 20)}..." to cart`);
    setTimeout(() => setOrderNotification(''), 3000);
  };

  const handleCheckout = (paymentMethod: string) => {
    const subtotal = cart.reduce((acc, item) => acc + item.product.price * item.quantity, 0);
    const newOrder = {
      id: `CS-ZA-${Math.floor(1000 + Math.random() * 9000)}`,
      date: 'Just now',
      items: cart.map(i => `${i.product.title} (x${i.quantity})`).join(', '),
      total: subtotal,
      payment: paymentMethod,
      status: paymentMethod.includes('Escrow') ? 'Escrow Protected - Awaiting Handover' : 'Confirmed',
      safeSpot: 'Library Security Foyer (CCTV Covered)'
    };
    setOrders([newOrder, ...orders]);
    setCart([]);
    setIsCartOpen(false);
    setActiveTab('orders');
    alert(`Order #${newOrder.id} confirmed! Payment of R ${subtotal.toFixed(2)} is held securely in Campus Escrow.`);
  };

  // Filtered products
  const filteredProducts = INITIAL_PRODUCTS.filter(p => {
    const matchesCategory = selectedCategory === 'All Items' || p.category === selectedCategory;
    const matchesCampus = selectedCampus === 'ALL' || p.campus === selectedCampus;
    const matchesSearch = !searchQuery || p.title.toLowerCase().includes(searchQuery.toLowerCase()) || p.desc.toLowerCase().includes(searchQuery.toLowerCase());
    return matchesCategory && matchesCampus && matchesSearch;
  });

  const cartTotal = cart.reduce((acc, item) => acc + item.product.price * item.quantity, 0);

  // If not logged in, show Student Email Login screen
  if (!user) {
    return (
      <div style={{ minHeight: '100vh', background: 'linear-gradient(135deg, #0f172a 0%, #004d30 100%)', display: 'flex', alignItems: 'center', justifyContent: 'center', padding: '20px' }}>
        <div style={{ background: '#ffffff', borderRadius: '24px', width: '100%', maxWidth: '480px', padding: '36px', boxShadow: '0 25px 50px -12px rgba(0, 0, 0, 0.25)' }}>
          <div style={{ textAlign: 'center', marginBottom: '24px' }}>
            <div style={{ width: '64px', height: '64px', background: '#007A4D', color: '#fff', borderRadius: '18px', display: 'inline-flex', alignItems: 'center', justifyContent: 'center', fontSize: '30px', marginBottom: '14px' }}>
              🎓
            </div>
            <h1 style={{ fontSize: '24px', fontWeight: '800', color: '#0f172a', marginBottom: '6px' }}>Student Email Login</h1>
            <p style={{ fontSize: '13px', color: '#64748b' }}>Community Store • South African University Marketplace</p>
          </div>

          <div style={{ background: '#e6f4ea', border: '1px solid #bbf7d0', borderRadius: '12px', padding: '12px', marginBottom: '20px', display: 'flex', gap: '10px' }}>
            <span style={{ fontSize: '18px' }}>🛡️</span>
            <div style={{ fontSize: '12px', color: '#166534', lineHeight: '1.4' }}>
              <strong>Verified .ac.za Domain SSO:</strong> No phone numbers or SMS OTP required. Authenticate directly with your university student email.
            </div>
          </div>

          <form onSubmit={handleLogin} style={{ display: 'flex', flexDirection: 'column', gap: '14px' }}>
            <div>
              <label style={{ display: 'block', fontSize: '12px', fontWeight: '600', color: '#475569', marginBottom: '6px' }}>Quick Select University Domain:</label>
              <div style={{ display: 'flex', flexWrap: 'wrap', gap: '6px' }}>
                {UNIVERSITIES.map(u => (
                  <button
                    key={u.id}
                    type="button"
                    onClick={() => handleDomainSelect(u.domain, u.id)}
                    style={{ background: '#f1f5f9', border: '1px solid #cbd5e1', borderRadius: '8px', padding: '6px 10px', fontSize: '11px', cursor: 'pointer', fontWeight: '600' }}
                  >
                    {u.id} ({u.domain})
                  </button>
                ))}
              </div>
            </div>

            <div>
              <label style={{ display: 'block', fontSize: '12px', fontWeight: '600', color: '#475569', marginBottom: '6px' }}>Student University Email (.ac.za)</label>
              <input
                type="email"
                required
                value={loginEmail}
                onChange={e => setLoginEmail(e.target.value)}
                placeholder="e.g. n.khumalo@myuct.ac.za"
                style={{ width: '100%', padding: '12px 14px', borderRadius: '10px', border: '1px solid #cbd5e1', fontSize: '14px' }}
              />
            </div>

            <div>
              <label style={{ display: 'block', fontSize: '12px', fontWeight: '600', color: '#475569', marginBottom: '6px' }}>Full Name</label>
              <input
                type="text"
                value={loginName}
                onChange={e => setLoginName(e.target.value)}
                placeholder="e.g. Nandi Khumalo"
                style={{ width: '100%', padding: '12px 14px', borderRadius: '10px', border: '1px solid #cbd5e1', fontSize: '14px' }}
              />
            </div>

            <div>
              <label style={{ display: 'block', fontSize: '12px', fontWeight: '600', color: '#475569', marginBottom: '6px' }}>Student Number</label>
              <input
                type="text"
                value={loginId}
                onChange={e => setLoginId(e.target.value)}
                placeholder="e.g. KHMNDI004"
                style={{ width: '100%', padding: '12px 14px', borderRadius: '10px', border: '1px solid #cbd5e1', fontSize: '14px' }}
              />
            </div>

            <div>
              <label style={{ display: 'block', fontSize: '12px', fontWeight: '600', color: '#475569', marginBottom: '6px' }}>Student Password</label>
              <input
                type="password"
                required
                value={loginPassword}
                onChange={e => setLoginPassword(e.target.value)}
                style={{ width: '100%', padding: '12px 14px', borderRadius: '10px', border: '1px solid #cbd5e1', fontSize: '14px' }}
              />
            </div>

            <button
              type="submit"
              style={{ width: '100%', padding: '14px', background: '#007A4D', color: '#ffffff', border: 'none', borderRadius: '12px', fontSize: '15px', fontWeight: '700', cursor: 'pointer', marginTop: '6px' }}
            >
              Sign In with Student Email
            </button>
          </form>

          <div style={{ marginTop: '20px', textAlign: 'center', borderTop: '1px solid #e2e8f0', paddingTop: '16px' }}>
            <span style={{ fontSize: '12px', color: '#64748b' }}>🇿🇦 UCT • Wits • Stellenbosch • UP • UJ • Rhodes</span>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div style={{ minHeight: '100vh', background: '#f8faf9', display: 'flex', flexDirection: 'column' }}>
      {/* Top Navbar */}
      <header style={{ background: '#ffffff', borderBottom: '1px solid #e2e8f0', position: 'sticky', top: 0, zIndex: 100 }}>
        <div style={{ maxWidth: '1240px', margin: '0 auto', padding: '12px 24px', display: 'flex', alignItems: 'center', justifyContent: 'space-between', gap: '20px' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '12px', cursor: 'pointer' }} onClick={() => setActiveTab('store')}>
            <div style={{ width: '40px', height: '40px', background: '#007A4D', color: '#fff', borderRadius: '10px', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: '20px' }}>
              🇿🇦
            </div>
            <div>
              <div style={{ fontWeight: '800', fontSize: '17px', color: '#0f172a' }}>Community Store</div>
              <div style={{ fontSize: '11px', color: '#059669', fontWeight: '600' }}>Campus & Local Marketplace</div>
            </div>
          </div>

          <div style={{ display: 'flex', gap: '8px' }}>
            <button
              onClick={() => setActiveTab('store')}
              style={{ background: activeTab === 'store' ? '#007A4D' : 'transparent', color: activeTab === 'store' ? '#fff' : '#475569', border: 'none', padding: '8px 16px', borderRadius: '8px', fontWeight: '600', cursor: 'pointer', fontSize: '13px' }}
            >
              Marketplace
            </button>
            <button
              onClick={() => setActiveTab('bulletin')}
              style={{ background: activeTab === 'bulletin' ? '#007A4D' : 'transparent', color: activeTab === 'bulletin' ? '#fff' : '#475569', border: 'none', padding: '8px 16px', borderRadius: '8px', fontWeight: '600', cursor: 'pointer', fontSize: '13px' }}
            >
              Campus Bulletin
            </button>
            <button
              onClick={() => setActiveTab('orders')}
              style={{ background: activeTab === 'orders' ? '#007A4D' : 'transparent', color: activeTab === 'orders' ? '#fff' : '#475569', border: 'none', padding: '8px 16px', borderRadius: '8px', fontWeight: '600', cursor: 'pointer', fontSize: '13px' }}
            >
              Orders & Escrow ({orders.length})
            </button>
            <button
              onClick={() => setActiveTab('proposal')}
              style={{ background: activeTab === 'proposal' ? '#007A4D' : 'transparent', color: activeTab === 'proposal' ? '#fff' : '#475569', border: 'none', padding: '8px 16px', borderRadius: '8px', fontWeight: '600', cursor: 'pointer', fontSize: '13px' }}
            >
              Project Proposal
            </button>
            <a
              href="/vue"
              style={{ background: '#42b883', color: '#ffffff', textDecoration: 'none', padding: '8px 14px', borderRadius: '8px', fontWeight: '700', fontSize: '12px', display: 'flex', alignItems: 'center', gap: '4px' }}
            >
              Switch to Vue 3 ➔
            </a>
          </div>

          <div style={{ display: 'flex', alignItems: 'center', gap: '16px' }}>
            <button
              onClick={() => setIsCartOpen(true)}
              style={{ background: '#f1f5f9', border: '1px solid #cbd5e1', padding: '8px 14px', borderRadius: '10px', cursor: 'pointer', fontWeight: '700', fontSize: '13px', display: 'flex', alignItems: 'center', gap: '6px' }}
            >
              🛒 Cart <span style={{ background: '#007A4D', color: '#fff', borderRadius: '10px', padding: '2px 8px', fontSize: '11px' }}>{cart.length}</span>
            </button>

            <div style={{ display: 'flex', alignItems: 'center', gap: '8px', background: '#f8fafc', padding: '6px 12px', borderRadius: '10px', border: '1px solid #e2e8f0' }}>
              <div style={{ fontSize: '12px' }}>
                <span style={{ fontWeight: '700' }}>{user.name}</span>
                <span style={{ display: 'block', fontSize: '10px', color: '#059669', fontWeight: '600' }}>🎓 {user.email}</span>
              </div>
              <button
                onClick={() => setUser(null)}
                style={{ background: 'transparent', border: 'none', color: '#dc2626', cursor: 'pointer', fontSize: '12px', fontWeight: '600', marginLeft: '6px' }}
              >
                Sign Out
              </button>
            </div>
          </div>
        </div>
      </header>

      {/* Main Content Area */}
      <main style={{ maxWidth: '1240px', margin: '0 auto', padding: '24px', flex: 1, width: '100%' }}>
        {orderNotification && (
          <div style={{ background: '#007A4D', color: '#fff', padding: '12px 20px', borderRadius: '10px', marginBottom: '16px', fontWeight: '600', fontSize: '14px' }}>
            {orderNotification}
          </div>
        )}

        {activeTab === 'store' && (
          <div>
            {/* Hero Banner */}
            <div style={{ background: 'linear-gradient(rgba(0,0,0,0.6), rgba(0,0,0,0.85)), url(https://images.unsplash.com/photo-1541339907198-e08756dedf3f?auto=format&fit=crop&w=1200&q=80)', backgroundSize: 'cover', backgroundPosition: 'center', color: '#fff', borderRadius: '20px', padding: '40px', marginBottom: '24px' }}>
              <span style={{ background: '#007A4D', padding: '4px 12px', borderRadius: '6px', fontSize: '12px', fontWeight: '700' }}>🇿🇦 MZANSI CAMPUS STORE</span>
              <h2 style={{ fontSize: '28px', fontWeight: '800', marginTop: '12px', marginBottom: '8px' }}>Trusted South African Student & Local Marketplace</h2>
              <p style={{ fontSize: '14px', maxWidth: '640px', opacity: 0.9 }}>
                Exchange textbooks, electronics, dorm essentials, and local farm goods securely with verified university students and vendors.
              </p>
            </div>

            {/* Filter Bar */}
            <div style={{ display: 'flex', flexWrap: 'wrap', gap: '12px', alignItems: 'center', justifyContent: 'space-between', marginBottom: '24px', background: '#fff', padding: '16px', borderRadius: '16px', border: '1px solid #e2e8f0' }}>
              <input
                type="text"
                value={searchQuery}
                onChange={e => setSearchQuery(e.target.value)}
                placeholder="Search textbooks, dorm tech, produce..."
                style={{ flex: 1, minWidth: '240px', padding: '10px 14px', borderRadius: '10px', border: '1px solid #cbd5e1', fontSize: '14px' }}
              />

              <select
                value={selectedCampus}
                onChange={e => setSelectedCampus(e.target.value)}
                style={{ padding: '10px 14px', borderRadius: '10px', border: '1px solid #cbd5e1', fontSize: '13px', fontWeight: '600', background: '#f8fafc' }}
              >
                <option value="ALL">All SA Campuses</option>
                {UNIVERSITIES.map(u => (
                  <option key={u.id} value={u.id}>{u.name}</option>
                ))}
              </select>
            </div>

            {/* Category Pills */}
            <div style={{ display: 'flex', flexWrap: 'wrap', gap: '8px', marginBottom: '24px' }}>
              {CATEGORIES.map(cat => (
                <button
                  key={cat}
                  onClick={() => setSelectedCategory(cat)}
                  style={{
                    background: selectedCategory === cat ? '#007A4D' : '#ffffff',
                    color: selectedCategory === cat ? '#ffffff' : '#475569',
                    border: '1px solid #e2e8f0',
                    padding: '8px 16px',
                    borderRadius: '20px',
                    fontWeight: '600',
                    fontSize: '12px',
                    cursor: 'pointer'
                  }}
                >
                  {cat}
                </button>
              ))}
            </div>

            {/* Product Grid */}
            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))', gap: '20px' }}>
              {filteredProducts.map(p => (
                <div key={p.id} style={{ background: '#ffffff', borderRadius: '16px', border: '1px solid #e2e8f0', overflow: 'hidden', display: 'flex', flexDirection: 'column' }}>
                  <div style={{ height: '170px', position: 'relative' }}>
                    <img src={p.img} alt={p.title} style={{ width: '100%', height: '100%', objectFit: 'cover' }} />
                    <span style={{ position: 'absolute', top: '10px', left: '10px', background: 'rgba(0,0,0,0.7)', color: '#fff', padding: '4px 8px', borderRadius: '6px', fontSize: '11px', fontWeight: '700' }}>
                      {p.campus}
                    </span>
                    <span style={{ position: 'absolute', top: '10px', right: '10px', background: '#dcfce7', color: '#15803d', padding: '4px 8px', borderRadius: '6px', fontSize: '10px', fontWeight: '700' }}>
                      -{p.ecoKg}kg CO₂
                    </span>
                  </div>

                  <div style={{ padding: '16px', display: 'flex', flexDirection: 'column', flex: 1 }}>
                    <div style={{ fontSize: '11px', color: '#64748b', fontWeight: '600', marginBottom: '4px' }}>{p.category} • {p.condition}</div>
                    <h3 style={{ fontSize: '15px', fontWeight: '700', color: '#0f172a', marginBottom: '8px', lineHeight: '1.3' }}>{p.title}</h3>
                    <p style={{ fontSize: '12px', color: '#64748b', marginBottom: '12px', flex: 1 }}>{p.desc}</p>

                    <div style={{ background: '#fef3c7', padding: '8px 10px', borderRadius: '8px', fontSize: '11px', color: '#92400e', marginBottom: '14px' }}>
                      📍 <strong>Safe Meetup:</strong> {p.safeSpot}
                    </div>

                    <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', borderTop: '1px solid #f1f5f9', paddingTop: '12px' }}>
                      <div>
                        <span style={{ fontSize: '18px', fontWeight: '800', color: '#007A4D' }}>R {p.price.toFixed(2)}</span>
                        {p.origPrice && <span style={{ fontSize: '12px', color: '#94a3b8', textDecoration: 'line-through', marginLeft: '6px' }}>R {p.origPrice}</span>}
                      </div>

                      <button
                        onClick={() => addToCart(p)}
                        style={{ background: '#007A4D', color: '#fff', border: 'none', padding: '8px 16px', borderRadius: '8px', fontWeight: '700', fontSize: '12px', cursor: 'pointer' }}
                      >
                        + Add to Cart
                      </button>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}

        {/* Orders & Escrow Tab */}
        {activeTab === 'orders' && (
          <div style={{ background: '#fff', borderRadius: '16px', padding: '24px', border: '1px solid #e2e8f0' }}>
            <h2 style={{ fontSize: '20px', fontWeight: '800', marginBottom: '8px' }}>My Orders & Campus Escrow Protection</h2>
            <p style={{ fontSize: '13px', color: '#64748b', marginBottom: '20px' }}>
              Funds are held securely in escrow until you inspect the item in person at the campus CCTV meeting zone and confirm release.
            </p>

            <div style={{ display: 'flex', flexDirection: 'column', gap: '14px' }}>
              {orders.map(order => (
                <div key={order.id} style={{ border: '1px solid #cbd5e1', borderRadius: '12px', padding: '16px', display: 'flex', justifyContent: 'space-between', alignItems: 'center', background: '#f8fafc' }}>
                  <div>
                    <span style={{ fontWeight: '800', fontSize: '14px' }}>Order #{order.id}</span>
                    <span style={{ fontSize: '11px', background: '#dcfce7', color: '#15803d', padding: '2px 8px', borderRadius: '6px', fontWeight: '700', marginLeft: '10px' }}>
                      {order.status}
                    </span>
                    <div style={{ fontSize: '13px', color: '#334155', marginTop: '6px' }}>Items: {order.items}</div>
                    <div style={{ fontSize: '11px', color: '#64748b', marginTop: '2px' }}>📍 Meeting Point: {order.safeSpot} • {order.payment}</div>
                  </div>

                  <div style={{ textAlign: 'right' }}>
                    <div style={{ fontSize: '18px', fontWeight: '800', color: '#007A4D', marginBottom: '6px' }}>R {order.total.toFixed(2)}</div>
                    <button
                      onClick={() => alert(`Confirmed! Escrow funds released to the seller for Order #${order.id}.`)}
                      style={{ background: '#059669', color: '#fff', border: 'none', padding: '6px 14px', borderRadius: '8px', fontSize: '12px', fontWeight: '700', cursor: 'pointer' }}
                    >
                      ✓ Confirm & Release Escrow
                    </button>
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}

        {/* Campus Bulletin Tab */}
        {activeTab === 'bulletin' && (
          <div style={{ background: '#fff', borderRadius: '16px', padding: '24px', border: '1px solid #e2e8f0' }}>
            <h2 style={{ fontSize: '20px', fontWeight: '800', marginBottom: '8px' }}>Campus Community Bulletin</h2>
            <p style={{ fontSize: '13px', color: '#64748b', marginBottom: '20px' }}>Announcements, textbook donation drives, and peer tutoring networks.</p>

            <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
              <div style={{ border: '1px solid #e2e8f0', borderRadius: '12px', padding: '18px' }}>
                <span style={{ background: '#e0f2fe', color: '#0369a1', padding: '4px 8px', borderRadius: '6px', fontSize: '11px', fontWeight: '700' }}>UCT • ECO DRIVE</span>
                <h3 style={{ fontSize: '16px', fontWeight: '700', marginTop: '8px', marginBottom: '6px' }}>Semester 2 Textbook Charity Drive & Swap</h3>
                <p style={{ fontSize: '13px', color: '#475569' }}>Bring your used textbooks to Upper Campus outside Leslie Social! Bring 2, take 1, or donate to first-year bursary students. Free fair-trade coffee provided.</p>
              </div>

              <div style={{ border: '1px solid #e2e8f0', borderRadius: '12px', padding: '18px' }}>
                <span style={{ background: '#fef3c7', color: '#b45309', padding: '4px 8px', borderRadius: '6px', fontSize: '11px', fontWeight: '700' }}>STELLENBOSCH • MARKET</span>
                <h3 style={{ fontSize: '16px', fontWeight: '700', marginTop: '8px', marginBottom: '6px' }}>Weekly Maties Farmers & Artisanal Pop-up</h3>
                <p style={{ fontSize: '13px', color: '#475569' }}>Support local Western Cape family farms. Fresh sourdough, raw fynbos honey, avocados, and student thrift stalls this Thursday on the Rooiplein.</p>
              </div>
            </div>
          </div>
        )}

        {/* Project Proposal Tab */}
        {activeTab === 'proposal' && (
          <div style={{ background: '#fff', borderRadius: '16px', padding: '28px', border: '1px solid #e2e8f0' }}>
            <h2 style={{ fontSize: '22px', fontWeight: '800', marginBottom: '10px' }}>Community Store Project Proposal (Academic Specs)</h2>
            <p style={{ fontSize: '13px', color: '#64748b', marginBottom: '24px' }}>Full project management deliverables, technical architecture, and risk matrix.</p>

            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(320px, 1fr))', gap: '20px' }}>
              <div style={{ background: '#f8fafc', padding: '16px', borderRadius: '12px', border: '1px solid #e2e8f0' }}>
                <h3 style={{ fontSize: '15px', fontWeight: '700', color: '#007A4D', marginBottom: '8px' }}>Phase 1: Discovery & Planning (Months 1-2)</h3>
                <p style={{ fontSize: '12px', color: '#475569', lineHeight: '1.5' }}>Engage campus student representative councils (SRCs), synthesize multi-role requirements (.ac.za & CIPC verification), and build security risk register.</p>
              </div>

              <div style={{ background: '#f8fafc', padding: '16px', borderRadius: '12px', border: '1px solid #e2e8f0' }}>
                <h3 style={{ fontSize: '15px', fontWeight: '700', color: '#007A4D', marginBottom: '8px' }}>Phase 2: Development Management (Months 3-5)</h3>
                <p style={{ fontSize: '12px', color: '#475569', lineHeight: '1.5' }}>Bi-weekly iterative sprints for responsive web/mobile clients, PayFast & SnapScan escrow payment pipelines, and community bulletin board.</p>
              </div>

              <div style={{ background: '#f8fafc', padding: '16px', borderRadius: '12px', border: '1px solid #e2e8f0' }}>
                <h3 style={{ fontSize: '15px', fontWeight: '700', color: '#007A4D', marginBottom: '8px' }}>Phase 3: Testing, Deployment & Closure (Months 6-7)</h3>
                <p style={{ fontSize: '12px', color: '#475569', lineHeight: '1.5' }}>Alpha trials with UCT & Wits student unions, security penetration testing, final academic project evaluation, and cloud production release.</p>
              </div>
            </div>
          </div>
        )}
      </main>

      {/* Cart Drawer */}
      {isCartOpen && (
        <div style={{ position: 'fixed', top: 0, right: 0, bottom: 0, width: '380px', background: '#fff', boxShadow: '-10px 0 30px rgba(0,0,0,0.15)', zIndex: 1000, padding: '24px', display: 'flex', flexDirection: 'column' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
            <h3 style={{ fontSize: '18px', fontWeight: '800' }}>Cart ({cart.length})</h3>
            <button onClick={() => setIsCartOpen(false)} style={{ background: 'transparent', border: 'none', fontSize: '18px', cursor: 'pointer' }}>✕</button>
          </div>

          <div style={{ flex: 1, overflowY: 'auto', display: 'flex', flexDirection: 'column', gap: '12px' }}>
            {cart.length === 0 ? (
              <div style={{ textAlign: 'center', color: '#94a3b8', marginTop: '40px' }}>Your cart is empty.</div>
            ) : (
              cart.map(item => (
                <div key={item.product.id} style={{ borderBottom: '1px solid #f1f5f9', paddingBottom: '10px' }}>
                  <div style={{ fontWeight: '700', fontSize: '13px' }}>{item.product.title}</div>
                  <div style={{ display: 'flex', justifyContent: 'space-between', marginTop: '4px', fontSize: '12px' }}>
                    <span>Qty: {item.quantity}</span>
                    <strong style={{ color: '#007A4D' }}>R {(item.product.price * item.quantity).toFixed(2)}</strong>
                  </div>
                </div>
              ))
            )}
          </div>

          {cart.length > 0 && (
            <div style={{ borderTop: '1px solid #e2e8f0', paddingTop: '16px' }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '16px', fontWeight: '800', marginBottom: '14px' }}>
                <span>Total:</span>
                <span style={{ color: '#007A4D' }}>R {cartTotal.toFixed(2)}</span>
              </div>

              <div style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
                <button
                  onClick={() => handleCheckout('Campus Escrow (SnapScan QR)')}
                  style={{ width: '100%', padding: '12px', background: '#0284c7', color: '#fff', border: 'none', borderRadius: '10px', fontWeight: '700', cursor: 'pointer', fontSize: '13px' }}
                >
                  Pay via SnapScan (Escrow)
                </button>
                <button
                  onClick={() => handleCheckout('Campus Escrow (PayFast Instant EFT)')}
                  style={{ width: '100%', padding: '12px', background: '#dc2626', color: '#fff', border: 'none', borderRadius: '10px', fontWeight: '700', cursor: 'pointer', fontSize: '13px' }}
                >
                  Pay via PayFast EFT (Escrow)
                </button>
              </div>
            </div>
          )}
        </div>
      )}
    </div>
  );
}
