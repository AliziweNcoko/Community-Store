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

// Sample Listings with rich South African market pictures
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
    title: 'Karoo Traditional Beef Biltong & Droëwors Craft Pack (500g)',
    desc: 'Freshly cured and spiced traditional Western Cape beef biltong and droëwors. High protein study snack.',
    price: 165,
    origPrice: 240,
    category: 'Local Produce & Food',
    campus: 'UCT',
    condition: 'Fresh / New',
    seller: 'Karoo Heritage Meats',
    role: 'Verified Vendor (CIPC)',
    rating: 4.99,
    safeSpot: 'Leslie Social Science Plaza Stalls',
    ecoKg: 2.5,
    img: 'https://images.unsplash.com/photo-1544025162-d76694265947?auto=format&fit=crop&w=600&q=80'
  },
  {
    id: 'prod-5',
    title: 'Organic Student Veggie Box & Artisanal Sourdough',
    desc: 'Fresh farm produce: avocados, baby spinach, heirloom tomatoes, and freshly baked sourdough loaf.',
    price: 180,
    origPrice: 260,
    category: 'Local Produce & Food',
    campus: 'STELLENBOSCH',
    condition: 'Fresh / New',
    seller: 'Fynbos Community Farm',
    role: 'Verified Vendor (CIPC)',
    rating: 4.98,
    safeSpot: 'Rooiplein Central Walkway',
    ecoKg: 12.0,
    img: 'https://images.unsplash.com/photo-1610348725531-843dff563e2c?auto=format&fit=crop&w=600&q=80'
  },
  {
    id: 'prod-6',
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
  }
];

export default function App() {
  // Auth state
  const [user, setUser] = useState<{ email: string; name: string; campus: string; studentId: string } | null>(null);
  const [isSignUp, setIsSignUp] = useState(false);
  const [loginEmail, setLoginEmail] = useState('n.khumalo@myuct.ac.za');
  const [loginPassword, setLoginPassword] = useState('Student2026!');
  const [loginName, setLoginName] = useState('Nandi Khumalo');
  const [loginId, setLoginId] = useState('KHMNDI004');

  // Outlook Verification PIN flow state
  const [isWaitingForPin, setIsWaitingForPin] = useState(false);
  const [enteredPin, setEnteredPin] = useState('');
  const [outlookNotification, setOutlookNotification] = useState('');
  const [showOutlookModal, setShowOutlookModal] = useState(false);
  const [pinError, setPinError] = useState('');
  const [deviceMode, setDeviceMode] = useState<'auto' | 'laptop' | 'phone'>('auto');
  const [selectedMarketPic, setSelectedMarketPic] = useState(0);
  const generatedPin = '849201';

  const MARKET_PICTURES = [
    {
      title: 'Campus Craft & Startup Fair',
      tag: '🇿🇦 Craft & Artisan Stalls',
      desc: 'Handmade student jewelry, beadwork, and campus entrepreneur creations.',
      url: 'https://images.unsplash.com/photo-1544025162-d76694265947?auto=format&fit=crop&w=800&q=80'
    },
    {
      title: 'Campus Braai & Hot Food Stalls',
      tag: '🔥 Street Food & Braai',
      desc: 'Flame-grilled boerewors rolls, samosas, koeksisters, and artisanal baked goods.',
      url: 'https://images.unsplash.com/photo-1555939594-58d7cb561ad1?auto=format&fit=crop&w=800&q=80'
    },
    {
      title: 'Student Thrift & Flea Bazaar',
      tag: '🌿 Thrift & Vintage Fashion',
      desc: 'Pre-loved clothing, recycled textbook racks, and sustainable zero-waste fashion.',
      url: 'https://images.unsplash.com/photo-1489987707025-afc232f7ea0f?auto=format&fit=crop&w=800&q=80'
    },
    {
      title: 'Organic Produce & Biltong Market',
      tag: '🥑 Farm Fresh & Droëwors',
      desc: 'Fresh avocados, sourdough bread, raw honey, and traditional cured biltong.',
      url: 'https://images.unsplash.com/photo-1610348725531-843dff563e2c?auto=format&fit=crop&w=800&q=80'
    },
    {
      title: 'Jacaranda Campus Walkway Stalls',
      tag: '🏛️ University Campus Quad',
      desc: 'Sunny university plaza trade connecting students, faculty, and local vendors.',
      url: 'https://images.unsplash.com/photo-1541829070764-84a7d30dd3f3?auto=format&fit=crop&w=800&q=80'
    },
    {
      title: 'Academic Textbooks & Study Desks',
      tag: '📚 Textbooks & Dorm Tech',
      desc: 'Calculus, Law, and Medicine prescribed course notes and study accessories.',
      url: 'https://images.unsplash.com/photo-1497633762265-9d179a990aa6?auto=format&fit=crop&w=800&q=80'
    }
  ];

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

  const handleSendOutlookPin = (e: React.FormEvent) => {
    e.preventDefault();
    if (!loginEmail) return;
    setIsWaitingForPin(true);
    setPinError('');
    setOutlookNotification(`📧 Microsoft Outlook Webmail: [Community Store] Verification PIN is: ${generatedPin} (Sent to ${loginEmail})`);
    setShowOutlookModal(true);
  };

  const handleVerifyPinAndRegister = () => {
    if (enteredPin.trim() === generatedPin) {
      setUser({
        email: loginEmail,
        name: loginName || 'Verified Student',
        campus: loginEmail.includes('wits') ? 'WITS' : loginEmail.includes('sun') ? 'STELLENBOSCH' : 'UCT',
        studentId: loginId || 'STU-1004'
      });
      setOutlookNotification('');
      setShowOutlookModal(false);
      setPinError('');
    } else {
      setPinError(`Incorrect PIN "${enteredPin}". Please check your Outlook inbox.`);
    }
  };

  const handleDirectLogin = (e: React.FormEvent) => {
    e.preventDefault();
    if (!loginEmail) return;
    setUser({
      email: loginEmail,
      name: loginName || 'Verified Student',
      campus: loginEmail.includes('wits') ? 'WITS' : loginEmail.includes('sun') ? 'STELLENBOSCH' : 'UCT',
      studentId: loginId || 'STU-1004'
    });
  };

  const handleQuickLogin = (email: string, campus: string) => {
    setLoginEmail(email);
    setUser({
      email,
      name: email.includes('khumalo') ? 'Nandi Khumalo' : email.includes('ndlovu') ? 'Sipho Ndlovu' : 'Anika van der Merwe',
      campus,
      studentId: 'STU-1004'
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

  // Responsive Login & Sign Up View (Laptop 2-columns & Phone stacked with Mode Switcher)
  if (!user) {
    const currentPic = MARKET_PICTURES[selectedMarketPic];
    const isPhoneForced = deviceMode === 'phone';
    const isLaptopForced = deviceMode === 'laptop';

    return (
      <div style={{ minHeight: '100vh', background: '#0f172a', display: 'flex', flexDirection: 'column' }}>
        {/* Device Mode Switcher Top Bar */}
        <div style={{ background: '#1e293b', borderBottom: '1px solid #334155', padding: '8px 20px', display: 'flex', alignItems: 'center', justifyContent: 'space-between', color: '#94a3b8', fontSize: '12px' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
            <span style={{ fontWeight: '700', color: '#f8fafc' }}>Layout Mode:</span>
            <button
              onClick={() => setDeviceMode('auto')}
              style={{ background: deviceMode === 'auto' ? '#007A4D' : '#334155', color: '#fff', border: 'none', padding: '4px 10px', borderRadius: '6px', fontSize: '11px', cursor: 'pointer', fontWeight: '600' }}
            >
              🔄 Auto-Detect
            </button>
            <button
              onClick={() => setDeviceMode('laptop')}
              style={{ background: deviceMode === 'laptop' ? '#007A4D' : '#334155', color: '#fff', border: 'none', padding: '4px 10px', borderRadius: '6px', fontSize: '11px', cursor: 'pointer', fontWeight: '600' }}
            >
              💻 Laptop View
            </button>
            <button
              onClick={() => setDeviceMode('phone')}
              style={{ background: deviceMode === 'phone' ? '#007A4D' : '#334155', color: '#fff', border: 'none', padding: '4px 10px', borderRadius: '6px', fontSize: '11px', cursor: 'pointer', fontWeight: '600' }}
            >
              📱 Phone View
            </button>
          </div>

          {isWaitingForPin && (
            <button
              onClick={() => setShowOutlookModal(true)}
              style={{ background: '#0078D4', color: '#fff', border: 'none', padding: '4px 12px', borderRadius: '6px', fontSize: '11px', fontWeight: '700', cursor: 'pointer', display: 'flex', alignItems: 'center', gap: '6px' }}
            >
              📬 View Outlook Message
            </button>
          )}
        </div>

        {/* Top Simulated Outlook Notification Banner */}
        {outlookNotification && (
          <div
            onClick={() => { setEnteredPin(generatedPin); setShowOutlookModal(true); }}
            style={{ background: '#0078D4', color: '#fff', padding: '12px 20px', display: 'flex', alignItems: 'center', justifyContent: 'space-between', cursor: 'pointer', zIndex: 100 }}
          >
            <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
              <span style={{ fontSize: '20px' }}>📬</span>
              <div>
                <strong style={{ fontSize: '13px' }}>{outlookNotification}</strong>
                <div style={{ fontSize: '11px', opacity: 0.9 }}>Click here to view full Outlook email or autofill PIN into verification box</div>
              </div>
            </div>
            <button onClick={(e) => { e.stopPropagation(); setOutlookNotification(''); }} style={{ background: 'transparent', border: 'none', color: '#fff', fontSize: '18px', cursor: 'pointer' }}>✕</button>
          </div>
        )}

        <div style={{ flex: 1, display: 'flex', alignItems: 'center', justifyContent: 'center', padding: isPhoneForced ? '12px' : '24px' }}>
          <div
            style={{
              background: '#ffffff',
              borderRadius: isPhoneForced ? '28px' : '24px',
              width: '100%',
              maxWidth: isPhoneForced ? '420px' : isLaptopForced ? '1080px' : '980px',
              overflow: 'hidden',
              boxShadow: '0 25px 50px -12px rgba(0, 0, 0, 0.4)',
              display: isPhoneForced ? 'flex' : 'grid',
              flexDirection: isPhoneForced ? 'column' : undefined,
              gridTemplateColumns: isPhoneForced ? undefined : 'repeat(auto-fit, minmax(350px, 1fr))'
            }}
          >
            {/* Market Pictures & Campus Showcase Column / Header */}
            <div
              style={{
                position: 'relative',
                minHeight: isPhoneForced ? '200px' : '420px',
                background: `linear-gradient(rgba(0,0,0,0.3), rgba(0,0,0,0.85)), url(${currentPic.url})`,
                backgroundSize: 'cover',
                backgroundPosition: 'center',
                padding: isPhoneForced ? '18px' : '32px',
                color: '#fff',
                display: 'flex',
                flexDirection: 'column',
                justifyContent: 'space-between'
              }}
            >
              <div>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                  <span style={{ background: '#007A4D', color: '#fff', padding: '4px 10px', borderRadius: '6px', fontSize: '11px', fontWeight: '800' }}>
                    {currentPic.tag}
                  </span>
                  <span style={{ background: 'rgba(0,0,0,0.5)', padding: '3px 8px', borderRadius: '6px', fontSize: '10px' }}>
                    Photo {selectedMarketPic + 1} of {MARKET_PICTURES.length}
                  </span>
                </div>
                <h2 style={{ fontSize: isPhoneForced ? '18px' : '24px', fontWeight: '800', marginTop: '10px', lineHeight: '1.2' }}>
                  {currentPic.title}
                </h2>
                <p style={{ fontSize: isPhoneForced ? '11px' : '13px', opacity: 0.9, marginTop: '6px', lineHeight: '1.4' }}>
                  {currentPic.desc}
                </p>
              </div>

              <div>
                {/* Thumbnails to cycle market pictures */}
                <div style={{ marginTop: '14px', marginBottom: '10px' }}>
                  <div style={{ fontSize: '10px', fontWeight: '700', textTransform: 'uppercase', letterSpacing: '0.5px', marginBottom: '6px', opacity: 0.85 }}>
                    Tap to view more market pictures:
                  </div>
                  <div style={{ display: 'flex', gap: '6px', overflowX: 'auto', paddingBottom: '4px' }}>
                    {MARKET_PICTURES.map((pic, idx) => (
                      <div
                        key={idx}
                        onClick={() => setSelectedMarketPic(idx)}
                        style={{
                          width: isPhoneForced ? '52px' : '64px',
                          height: isPhoneForced ? '36px' : '44px',
                          borderRadius: '6px',
                          backgroundImage: `url(${pic.url})`,
                          backgroundSize: 'cover',
                          backgroundPosition: 'center',
                          border: selectedMarketPic === idx ? '2px solid #10b981' : '1px solid rgba(255,255,255,0.4)',
                          cursor: 'pointer',
                          flexShrink: 0
                        }}
                      />
                    ))}
                  </div>
                </div>

                {!isPhoneForced && (
                  <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '10px', marginBottom: '8px' }}>
                    <div style={{ background: 'rgba(255,255,255,0.15)', backdropFilter: 'blur(8px)', padding: '8px 12px', borderRadius: '8px' }}>
                      <div style={{ fontWeight: '800', fontSize: '13px' }}>100% Verified</div>
                      <div style={{ fontSize: '10px', opacity: 0.85 }}>.ac.za Student SSO</div>
                    </div>
                    <div style={{ background: 'rgba(255,255,255,0.15)', backdropFilter: 'blur(8px)', padding: '8px 12px', borderRadius: '8px' }}>
                      <div style={{ fontWeight: '800', fontSize: '13px' }}>Escrow Guard</div>
                      <div style={{ fontSize: '10px', opacity: 0.85 }}>SnapScan & PayFast</div>
                    </div>
                  </div>
                )}
              </div>
            </div>

            {/* Right Column / Bottom: Authentication Form */}
            <div style={{ padding: isPhoneForced ? '20px' : '32px', display: 'flex', flexDirection: 'column', justifyContent: 'center' }}>
              <div style={{ display: 'flex', gap: '8px', background: '#f1f5f9', padding: '4px', borderRadius: '12px', marginBottom: '16px' }}>
                <button
                  onClick={() => { setIsSignUp(false); setIsWaitingForPin(false); setPinError(''); }}
                  style={{ flex: 1, padding: '8px', borderRadius: '8px', border: 'none', background: !isSignUp ? '#fff' : 'transparent', fontWeight: '700', fontSize: '13px', cursor: 'pointer', boxShadow: !isSignUp ? '0 2px 4px rgba(0,0,0,0.05)' : 'none' }}
                >
                  Student Sign In
                </button>
                <button
                  onClick={() => { setIsSignUp(true); setPinError(''); }}
                  style={{ flex: 1, padding: '8px', borderRadius: '8px', border: 'none', background: isSignUp ? '#fff' : 'transparent', fontWeight: '700', fontSize: '13px', cursor: 'pointer', boxShadow: isSignUp ? '0 2px 4px rgba(0,0,0,0.05)' : 'none' }}
                >
                  Sign Up (Outlook PIN)
                </button>
              </div>

              {/* Quick Domain Selector */}
              <div style={{ marginBottom: '12px' }}>
                <div style={{ fontSize: '11px', fontWeight: '600', color: '#64748b', marginBottom: '6px' }}>Select Student Domain:</div>
                <div style={{ display: 'flex', flexWrap: 'wrap', gap: '6px' }}>
                  {UNIVERSITIES.map(u => (
                    <button
                      key={u.id}
                      type="button"
                      onClick={() => handleDomainSelect(u.domain, u.id)}
                      style={{ background: '#f8fafc', border: '1px solid #cbd5e1', borderRadius: '6px', padding: '3px 8px', fontSize: '11px', cursor: 'pointer', fontWeight: '600' }}
                    >
                      {u.id}
                    </button>
                  ))}
                </div>
              </div>

              {!isSignUp ? (
                /* Standard Sign In Form */
                <form onSubmit={handleDirectLogin} style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
                  <div>
                    <label style={{ display: 'block', fontSize: '12px', fontWeight: '600', color: '#475569', marginBottom: '4px' }}>Student Email (.ac.za)</label>
                    <input
                      type="email"
                      required
                      value={loginEmail}
                      onChange={e => setLoginEmail(e.target.value)}
                      placeholder="e.g. n.khumalo@myuct.ac.za"
                      style={{ width: '100%', boxSizing: 'border-box', padding: '10px 12px', borderRadius: '8px', border: '1px solid #cbd5e1', fontSize: '13px' }}
                    />
                  </div>

                  <div>
                    <label style={{ display: 'block', fontSize: '12px', fontWeight: '600', color: '#475569', marginBottom: '4px' }}>Password</label>
                    <input
                      type="password"
                      required
                      value={loginPassword}
                      onChange={e => setLoginPassword(e.target.value)}
                      style={{ width: '100%', boxSizing: 'border-box', padding: '10px 12px', borderRadius: '8px', border: '1px solid #cbd5e1', fontSize: '13px' }}
                    />
                  </div>

                  <button
                    type="submit"
                    style={{ width: '100%', padding: '11px', background: '#007A4D', color: '#ffffff', border: 'none', borderRadius: '8px', fontSize: '14px', fontWeight: '700', cursor: 'pointer', marginTop: '4px' }}
                  >
                    Log In with Student Email
                  </button>
                </form>
              ) : (
                /* Sign Up with Outlook PIN Verification */
                <div>
                  {!isWaitingForPin ? (
                    <form onSubmit={handleSendOutlookPin} style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>
                      <div>
                        <label style={{ display: 'block', fontSize: '11px', fontWeight: '600', color: '#475569', marginBottom: '3px' }}>Full Name</label>
                        <input
                          type="text"
                          required
                          value={loginName}
                          onChange={e => setLoginName(e.target.value)}
                          placeholder="e.g. Nandi Khumalo"
                          style={{ width: '100%', boxSizing: 'border-box', padding: '8px 10px', borderRadius: '8px', border: '1px solid #cbd5e1', fontSize: '13px' }}
                        />
                      </div>

                      <div>
                        <label style={{ display: 'block', fontSize: '11px', fontWeight: '600', color: '#475569', marginBottom: '3px' }}>Student Email (Outlook Webmail)</label>
                        <input
                          type="email"
                          required
                          value={loginEmail}
                          onChange={e => setLoginEmail(e.target.value)}
                          placeholder="e.g. n.khumalo@myuct.ac.za"
                          style={{ width: '100%', boxSizing: 'border-box', padding: '8px 10px', borderRadius: '8px', border: '1px solid #cbd5e1', fontSize: '13px' }}
                        />
                      </div>

                      <div>
                        <label style={{ display: 'block', fontSize: '11px', fontWeight: '600', color: '#475569', marginBottom: '3px' }}>Student ID Number</label>
                        <input
                          type="text"
                          required
                          value={loginId}
                          onChange={e => setLoginId(e.target.value)}
                          placeholder="e.g. KHMNDI004"
                          style={{ width: '100%', boxSizing: 'border-box', padding: '8px 10px', borderRadius: '8px', border: '1px solid #cbd5e1', fontSize: '13px' }}
                        />
                      </div>

                      <button
                        type="submit"
                        style={{ width: '100%', padding: '11px', background: '#0078D4', color: '#ffffff', border: 'none', borderRadius: '8px', fontSize: '13px', fontWeight: '700', cursor: 'pointer', marginTop: '4px' }}
                      >
                        📧 Send Verification PIN to Outlook
                      </button>
                    </form>
                  ) : (
                    /* Step 2: Enter 6-Digit PIN */
                    <div style={{ background: '#f0fdf4', border: '1px solid #bbf7d0', padding: '16px', borderRadius: '12px' }}>
                      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '4px' }}>
                        <div style={{ fontWeight: '800', color: '#166534', fontSize: '13px' }}>
                          Check Your Outlook Inbox
                        </div>
                        <button
                          type="button"
                          onClick={() => setShowOutlookModal(true)}
                          style={{ background: '#0078D4', color: '#fff', border: 'none', padding: '2px 8px', borderRadius: '4px', fontSize: '10px', fontWeight: '700', cursor: 'pointer' }}
                        >
                          Open Email
                        </button>
                      </div>

                      <p style={{ fontSize: '11px', color: '#15803d', marginBottom: '10px' }}>
                        We sent a 6-digit PIN to <strong>{loginEmail}</strong>. Enter the PIN below or tap autofill.
                      </p>

                      <input
                        type="text"
                        value={enteredPin}
                        onChange={e => { setEnteredPin(e.target.value); setPinError(''); }}
                        placeholder="Enter PIN (e.g. 849201)"
                        maxLength={6}
                        style={{ width: '100%', boxSizing: 'border-box', padding: '10px', textAlign: 'center', fontSize: '18px', letterSpacing: '4px', fontWeight: '800', borderRadius: '8px', border: pinError ? '2px solid #ef4444' : '2px solid #007A4D', marginBottom: '8px' }}
                      />

                      {pinError && (
                        <div style={{ color: '#ef4444', fontSize: '11px', fontWeight: '600', marginBottom: '8px', textAlign: 'center' }}>
                          {pinError}
                        </div>
                      )}

                      <button
                        type="button"
                        onClick={handleVerifyPinAndRegister}
                        disabled={!enteredPin}
                        style={{ width: '100%', padding: '10px', background: enteredPin ? '#007A4D' : '#94a3b8', color: '#ffffff', border: 'none', borderRadius: '8px', fontSize: '13px', fontWeight: '700', cursor: enteredPin ? 'pointer' : 'not-allowed' }}
                      >
                        ✓ Verify PIN & Complete Sign Up
                      </button>

                      <div style={{ display: 'flex', justifyContent: 'space-between', marginTop: '10px', fontSize: '11px' }}>
                        <button type="button" onClick={() => setEnteredPin(generatedPin)} style={{ background: 'transparent', border: 'none', color: '#0078D4', fontWeight: '700', cursor: 'pointer' }}>
                          ⚡ Autofill PIN ({generatedPin})
                        </button>
                        <button type="button" onClick={() => setIsWaitingForPin(false)} style={{ background: 'transparent', border: 'none', color: '#64748b', cursor: 'pointer' }}>
                          Edit Email
                        </button>
                      </div>
                    </div>
                  )}
                </div>
              )}

              {/* 1-Tap Fast Demo Accounts */}
              <div style={{ marginTop: '16px', borderTop: '1px solid #f1f5f9', paddingTop: '12px' }}>
                <div style={{ fontSize: '11px', fontWeight: '600', color: '#64748b', marginBottom: '6px' }}>1-Tap Demo Student Login:</div>
                <div style={{ display: 'flex', gap: '6px' }}>
                  <button
                    type="button"
                    onClick={() => handleQuickLogin('n.khumalo@myuct.ac.za', 'UCT')}
                    style={{ flex: 1, background: '#f8fafc', border: '1px solid #cbd5e1', borderRadius: '6px', padding: '5px', fontSize: '11px', cursor: 'pointer', fontWeight: '600' }}
                  >
                    UCT
                  </button>
                  <button
                    type="button"
                    onClick={() => handleQuickLogin('s.ndlovu@students.wits.ac.za', 'WITS')}
                    style={{ flex: 1, background: '#f8fafc', border: '1px solid #cbd5e1', borderRadius: '6px', padding: '5px', fontSize: '11px', cursor: 'pointer', fontWeight: '600' }}
                  >
                    Wits
                  </button>
                  <button
                    type="button"
                    onClick={() => handleQuickLogin('a.vandermerwe@sun.ac.za', 'STELLENBOSCH')}
                    style={{ flex: 1, background: '#f8fafc', border: '1px solid #cbd5e1', borderRadius: '6px', padding: '5px', fontSize: '11px', cursor: 'pointer', fontWeight: '600' }}
                  >
                    Maties
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>

        {/* Realistic Microsoft Outlook Inbox Dialog Modal */}
        {showOutlookModal && (
          <div style={{ position: 'fixed', inset: 0, background: 'rgba(0,0,0,0.65)', display: 'flex', alignItems: 'center', justifyContent: 'center', padding: '16px', zIndex: 999 }}>
            <div style={{ background: '#ffffff', borderRadius: '16px', width: '100%', maxWidth: '540px', overflow: 'hidden', boxShadow: '0 25px 50px -12px rgba(0,0,0,0.5)' }}>
              {/* Outlook Blue Header */}
              <div style={{ background: '#0078D4', color: '#fff', padding: '14px 20px', display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
                  <span style={{ fontSize: '22px' }}>📬</span>
                  <div>
                    <div style={{ fontWeight: '800', fontSize: '15px' }}>Microsoft 365 Outlook</div>
                    <div style={{ fontSize: '11px', opacity: 0.9 }}>Student Webmail • Exchange Online</div>
                  </div>
                </div>
                <button
                  onClick={() => setShowOutlookModal(false)}
                  style={{ background: 'transparent', border: 'none', color: '#fff', fontSize: '20px', cursor: 'pointer' }}
                >
                  ✕
                </button>
              </div>

              {/* Email Content Box */}
              <div style={{ padding: '20px' }}>
                <div style={{ background: '#f8fafc', border: '1px solid #e2e8f0', borderRadius: '10px', padding: '12px', marginBottom: '14px', fontSize: '12px' }}>
                  <div style={{ color: '#64748b' }}>From: <strong style={{ color: '#0f172a' }}>verification@communitystore.ac.za</strong></div>
                  <div style={{ color: '#64748b' }}>To: <strong style={{ color: '#0f172a' }}>{loginEmail}</strong></div>
                  <div style={{ color: '#64748b', marginTop: '4px' }}>Subject: <strong style={{ color: '#0078D4' }}>🔐 Your Community Store Verification PIN: {generatedPin}</strong></div>
                </div>

                <div style={{ fontSize: '13px', color: '#334155', lineHeight: '1.5', marginBottom: '16px' }}>
                  <p>Dumelang / Molo / Hello {loginName || 'Student'},</p>
                  <p>Welcome to the <strong>Community Store Campus Marketplace</strong>. Use the 6-digit one-time security PIN below to complete your registration:</p>

                  <div style={{ background: '#e0f2fe', border: '2px dashed #0284c7', borderRadius: '12px', padding: '16px', textAlign: 'center', margin: '14px 0' }}>
                    <div style={{ fontSize: '11px', fontWeight: '700', color: '#0369a1', textTransform: 'uppercase', letterSpacing: '1px' }}>Your One-Time PIN</div>
                    <div style={{ fontSize: '32px', fontWeight: '900', color: '#0369a1', letterSpacing: '6px', margin: '6px 0' }}>{generatedPin}</div>
                    <div style={{ fontSize: '10px', color: '#0284c7' }}>Valid for 10 minutes • Keep this PIN confidential</div>
                  </div>

                  <p style={{ fontSize: '11px', color: '#64748b' }}>If you did not request this verification code, please ignore this email.</p>
                </div>

                <div style={{ display: 'flex', gap: '10px', justifyContent: 'flex-end' }}>
                  <button
                    onClick={() => setShowOutlookModal(false)}
                    style={{ padding: '8px 14px', background: '#f1f5f9', border: '1px solid #cbd5e1', borderRadius: '8px', fontSize: '12px', cursor: 'pointer', fontWeight: '600' }}
                  >
                    Close
                  </button>
                  <button
                    onClick={() => {
                      setEnteredPin(generatedPin);
                      setShowOutlookModal(false);
                    }}
                    style={{ padding: '8px 16px', background: '#0078D4', color: '#fff', border: 'none', borderRadius: '8px', fontSize: '12px', cursor: 'pointer', fontWeight: '700' }}
                  >
                    ⚡ Autofill PIN into Verification Box
                  </button>
                </div>
              </div>
            </div>
          </div>
        )}
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
            {/* Hero Banner with South African Market Scene */}
            <div style={{ background: 'linear-gradient(rgba(0,0,0,0.55), rgba(0,0,0,0.85)), url(https://images.unsplash.com/photo-1544025162-d76694265947?auto=format&fit=crop&w=1200&q=80)', backgroundSize: 'cover', backgroundPosition: 'center', color: '#fff', borderRadius: '20px', padding: '40px', marginBottom: '24px' }}>
              <span style={{ background: '#007A4D', padding: '4px 12px', borderRadius: '6px', fontSize: '12px', fontWeight: '700' }}>🇿🇦 MZANSI CAMPUS STORE</span>
              <h2 style={{ fontSize: '28px', fontWeight: '800', marginTop: '12px', marginBottom: '8px' }}>Trusted South African Student & Local Marketplace</h2>
              <p style={{ fontSize: '14px', maxWidth: '640px', opacity: 0.9 }}>
                Exchange textbooks, electronics, dorm essentials, Karoo biltong, and local farm goods securely with verified university students and vendors.
              </p>
            </div>

            {/* Filter Bar */}
            <div style={{ display: 'flex', flexWrap: 'wrap', gap: '12px', alignItems: 'center', justifyContent: 'space-between', marginBottom: '24px', background: '#fff', padding: '16px', borderRadius: '16px', border: '1px solid #e2e8f0' }}>
              <input
                type="text"
                value={searchQuery}
                onChange={e => setSearchQuery(e.target.value)}
                placeholder="Search textbooks, dorm tech, Karoo biltong, farm produce..."
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
                  {{ cat }}
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
