<template>
  <!-- Student Email Login & Sign Up View (Responsive Laptop 2-Col & Phone Stacked) -->
  <div v-if="!user" style="min-height: 100vh; background: #0f172a; display: flex; flex-direction: column;">
    <!-- Device Mode Switcher Top Bar -->
    <div style="background: #1e293b; border-bottom: 1px solid #334155; padding: 8px 20px; display: flex; align-items: center; justify-content: space-between; color: #94a3b8; font-size: 12px;">
      <div style="display: flex; align-items: center; gap: 8px;">
        <span style="font-weight: 700; color: #f8fafc;">Layout Mode:</span>
        <button
          @click="deviceMode = 'auto'"
          :style="{ background: deviceMode === 'auto' ? '#007A4D' : '#334155', color: '#fff', border: 'none', padding: '4px 10px', borderRadius: '6px', fontSize: '11px', cursor: 'pointer', fontWeight: '600' }"
        >
          🔄 Auto-Detect
        </button>
        <button
          @click="deviceMode = 'laptop'"
          :style="{ background: deviceMode === 'laptop' ? '#007A4D' : '#334155', color: '#fff', border: 'none', padding: '4px 10px', borderRadius: '6px', fontSize: '11px', cursor: 'pointer', fontWeight: '600' }"
        >
          💻 Laptop View
        </button>
        <button
          @click="deviceMode = 'phone'"
          :style="{ background: deviceMode === 'phone' ? '#007A4D' : '#334155', color: '#fff', border: 'none', padding: '4px 10px', borderRadius: '6px', fontSize: '11px', cursor: 'pointer', fontWeight: '600' }"
        >
          📱 Phone View
        </button>
      </div>

      <button
        v-if="isWaitingForPin"
        @click="showOutlookModal = true"
        style="background: #0078D4; color: #fff; border: none; padding: 4px 12px; borderRadius: 6px; fontSize: 11px; fontWeight: 700; cursor: pointer; display: flex; align-items: center; gap: 6px;"
      >
        📬 View Outlook Message
      </button>
    </div>

    <!-- Top Simulated Outlook Notification Banner -->
    <div
      v-if="outlookNotification"
      @click="enteredPin = generatedPin; showOutlookModal = true"
      style="background: #0078D4; color: #fff; padding: 12px 20px; display: flex; align-items: center; justify-content: space-between; cursor: pointer; z-index: 100;"
    >
      <div style="display: flex; align-items: center; gap: 10px;">
        <span style="font-size: 20px;">📬</span>
        <div>
          <strong style="font-size: 13px;">{{ outlookNotification }}</strong>
          <div style="font-size: 11px; opacity: 0.9;">Click here to view full Outlook email or autofill PIN into verification box</div>
        </div>
      </div>
      <button @click.stop="outlookNotification = ''" style="background: transparent; border: none; color: #fff; font-size: 18px; cursor: pointer;">✕</button>
    </div>

    <div :style="{ flex: 1, display: 'flex', alignItems: 'center', justifyContent: 'center', padding: deviceMode === 'phone' ? '12px' : '24px' }">
      <div
        :style="{
          background: '#ffffff',
          borderRadius: deviceMode === 'phone' ? '28px' : '24px',
          width: '100%',
          maxWidth: deviceMode === 'phone' ? '420px' : deviceMode === 'laptop' ? '1080px' : '980px',
          overflow: 'hidden',
          boxShadow: '0 25px 50px -12px rgba(0, 0, 0, 0.4)',
          display: deviceMode === 'phone' ? 'flex' : 'grid',
          flexDirection: deviceMode === 'phone' ? 'column' : undefined,
          gridTemplateColumns: deviceMode === 'phone' ? undefined : 'repeat(auto-fit, minmax(350px, 1fr))'
        }"
      >
        <!-- Left Column / Header: South African Market Pictures & Student Vibe -->
        <div
          :style="{
            position: 'relative',
            minHeight: deviceMode === 'phone' ? '200px' : '420px',
            background: `linear-gradient(rgba(0,0,0,0.3), rgba(0,0,0,0.85)), url(${currentPic.url})`,
            backgroundSize: 'cover',
            backgroundPosition: 'center',
            padding: deviceMode === 'phone' ? '18px' : '32px',
            color: '#fff',
            display: 'flex',
            flexDirection: 'column',
            justifyContent: 'space-between'
          }"
        >
          <div>
            <div style="display: flex; justify-content: space-between; align-items: center;">
              <span style="background: #007A4D; color: #fff; padding: 4px 10px; border-radius: 6px; font-size: 11px; font-weight: 800;">
                {{ currentPic.tag }}
              </span>
              <span style="background: rgba(0,0,0,0.5); padding: 3px 8px; border-radius: 6px; font-size: 10px;">
                Photo {{ selectedMarketPic + 1 }} of {{ MARKET_PICTURES.length }}
              </span>
            </div>
            <h2 :style="{ fontSize: deviceMode === 'phone' ? '18px' : '24px', fontWeight: '800', marginTop: '10px', lineHeight: '1.2' }">
              {{ currentPic.title }}
            </h2>
            <p :style="{ fontSize: deviceMode === 'phone' ? '11px' : '13px', opacity: 0.9, marginTop: '6px', lineHeight: '1.4' }">
              {{ currentPic.desc }}
            </p>
          </div>

          <div>
            <!-- Thumbnails to cycle market pictures -->
            <div style="margin-top: 14px; margin-bottom: 10px;">
              <div style="font-size: 10px; font-weight: 700; text-transform: uppercase; letter-spacing: 0.5px; margin-bottom: 6px; opacity: 0.85;">
                Tap to explore more market scenes:
              </div>
              <div style="display: flex; gap: 6px; overflow-x: auto; padding-bottom: 4px;">
                <div
                  v-for="(pic, idx) in MARKET_PICTURES"
                  :key="idx"
                  @click="selectedMarketPic = idx"
                  :style="{
                    width: deviceMode === 'phone' ? '52px' : '64px',
                    height: deviceMode === 'phone' ? '36px' : '44px',
                    borderRadius: '6px',
                    backgroundImage: `url(${pic.url})`,
                    backgroundSize: 'cover',
                    backgroundPosition: 'center',
                    border: selectedMarketPic === idx ? '2px solid #10b981' : '1px solid rgba(255,255,255,0.4)',
                    cursor: 'pointer',
                    flexShrink: 0
                  }"
                />
              </div>
            </div>

            <div v-if="deviceMode !== 'phone'" style="display: grid; grid-template-columns: 1fr 1fr; gap: 10px; margin-bottom: 8px;">
              <div style="background: rgba(255,255,255,0.15); backdrop-filter: blur(8px); padding: 8px 12px; border-radius: 8px;">
                <div style="font-weight: 800; font-size: 13px;">100% Verified</div>
                <div style="font-size: 10px; opacity: 0.85;">.ac.za Student SSO</div>
              </div>
              <div style="background: rgba(255,255,255,0.15); backdrop-filter: blur(8px); padding: 8px 12px; border-radius: 8px;">
                <div style="font-weight: 800; font-size: 13px;">Escrow Guard</div>
                <div style="font-size: 10px; opacity: 0.85;">SnapScan & PayFast</div>
              </div>
            </div>
          </div>
        </div>

        <!-- Right Column / Bottom: Login & Sign Up Form with Outlook PIN -->
        <div :style="{ padding: deviceMode === 'phone' ? '20px' : '32px', display: 'flex', flexDirection: 'column', justifyContent: 'center' }">
          <div style="display: flex; gap: 8px; background: #f1f5f9; padding: 4px; border-radius: 12px; margin-bottom: 16px;">
            <button
              @click="isSignUp = false; isWaitingForPin = false; pinError = '';"
              :style="{ flex: 1, padding: '8px', borderRadius: '8px', border: 'none', background: !isSignUp ? '#fff' : 'transparent', fontWeight: '700', fontSize: '13px', cursor: 'pointer', boxShadow: !isSignUp ? '0 2px 4px rgba(0,0,0,0.05)' : 'none' }"
            >
              Student Sign In
            </button>
            <button
              @click="isSignUp = true; pinError = '';"
              :style="{ flex: 1, padding: '8px', borderRadius: '8px', border: 'none', background: isSignUp ? '#fff' : 'transparent', fontWeight: '700', fontSize: '13px', cursor: 'pointer', boxShadow: isSignUp ? '0 2px 4px rgba(0,0,0,0.05)' : 'none' }"
            >
              Sign Up (Outlook PIN)
            </button>
          </div>

          <!-- Quick Domain Selector -->
          <div style="margin-bottom: 12px;">
            <div style="font-size: 11px; fontWeight: '600'; color: #64748b; margin-bottom: 6px;">Select Student Domain:</div>
            <div style="display: flex; flex-wrap: wrap; gap: 6px;">
              <button
                v-for="u in UNIVERSITIES"
                :key="u.id"
                type="button"
                @click="handleDomainSelect(u.domain)"
                style="background: #f8fafc; border: 1px solid #cbd5e1; border-radius: 6px; padding: 3px 8px; font-size: 11px; cursor: pointer; font-weight: 600;"
              >
                {{ u.id }}
              </button>
            </div>
          </div>

          <!-- Normal Sign In -->
          <form v-if="!isSignUp" @submit.prevent="handleDirectLogin" style="display: flex; flex-direction: column; gap: 12px;">
            <div>
              <label style="display: block; font-size: 12px; font-weight: 600; color: #475569; margin-bottom: 4px;">Student Email (.ac.za)</label>
              <input
                type="email"
                required
                v-model="loginEmail"
                placeholder="e.g. n.khumalo@myuct.ac.za"
                style="width: 100%; box-sizing: border-box; padding: 10px 12px; border-radius: 8px; border: 1px solid #cbd5e1; font-size: 13px;"
              />
            </div>

            <div>
              <label style="display: block; font-size: 12px; font-weight: 600; color: #475569; margin-bottom: 4px;">Password</label>
              <input
                type="password"
                required
                v-model="loginPassword"
                style="width: 100%; box-sizing: border-box; padding: 10px 12px; border-radius: 8px; border: 1px solid #cbd5e1; font-size: 13px;"
              />
            </div>

            <button
              type="submit"
              style="width: 100%; padding: 11px; background: #007A4D; color: #ffffff; border: none; border-radius: 8px; font-size: 14px; font-weight: 700; cursor: pointer; margin-top: 4px;"
            >
              Log In with Student Email
            </button>
          </form>

          <!-- Sign Up with Outlook PIN -->
          <div v-else>
            <form v-if="!isWaitingForPin" @submit.prevent="handleSendOutlookPin" style="display: flex; flex-direction: column; gap: 10px;">
              <div>
                <label style="display: block; font-size: 11px; font-weight: 600; color: #475569; margin-bottom: 3px;">Full Name</label>
                <input
                  type="text"
                  required
                  v-model="loginName"
                  placeholder="e.g. Nandi Khumalo"
                  style="width: 100%; box-sizing: border-box; padding: 8px 10px; border-radius: 8px; border: 1px solid #cbd5e1; font-size: 13px;"
                />
              </div>

              <div>
                <label style="display: block; font-size: 11px; font-weight: 600; color: #475569; margin-bottom: 3px;">Student Email (Outlook Webmail)</label>
                <input
                  type="email"
                  required
                  v-model="loginEmail"
                  placeholder="e.g. n.khumalo@myuct.ac.za"
                  style="width: 100%; box-sizing: border-box; padding: 8px 10px; border-radius: 8px; border: 1px solid #cbd5e1; font-size: 13px;"
                />
              </div>

              <div>
                <label style="display: block; font-size: 11px; font-weight: 600; color: #475569; margin-bottom: 3px;">Student ID Number</label>
                <input
                  type="text"
                  required
                  v-model="loginId"
                  placeholder="e.g. KHMNDI004"
                  style="width: 100%; box-sizing: border-box; padding: 8px 10px; border-radius: 8px; border: 1px solid #cbd5e1; font-size: 13px;"
                />
              </div>

              <button
                type="submit"
                style="width: 100%; padding: 11px; background: #0078D4; color: #ffffff; border: none; border-radius: 8px; font-size: 13px; fontWeight: 700; cursor: pointer; margin-top: 4px;"
              >
                📧 Send Verification PIN to Outlook
              </button>
            </form>

            <!-- Step 2: Enter 6-Digit PIN -->
            <div v-else style="background: #f0fdf4; border: 1px solid #bbf7d0; padding: 16px; border-radius: 12px;">
              <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 4px;">
                <div style="font-weight: 800; color: #166534; font-size: 13px;">
                  Check Your Outlook Inbox
                </div>
                <button
                  type="button"
                  @click="showOutlookModal = true"
                  style="background: #0078D4; color: #fff; border: none; padding: 2px 8px; border-radius: 4px; font-size: 10px; font-weight: 700; cursor: pointer;"
                >
                  Open Email
                </button>
              </div>

              <p style="font-size: 11px; color: #15803d; margin-bottom: 10px;">
                We sent a 6-digit PIN to <strong>{{ loginEmail }}</strong>. Enter the PIN below or tap autofill.
              </p>

              <input
                type="text"
                v-model="enteredPin"
                @input="pinError = ''"
                placeholder="Enter PIN (e.g. 849201)"
                maxlength="6"
                :style="{ width: '100%', boxSizing: 'border-box', padding: '10px', textAlign: 'center', fontSize: '18px', letterSpacing: '4px', fontWeight: '800', borderRadius: '8px', border: pinError ? '2px solid #ef4444' : '2px solid #007A4D', marginBottom: '8px' }"
              />

              <div v-if="pinError" style="color: #ef4444; font-size: 11px; font-weight: 600; margin-bottom: 8px; text-align: center;">
                {{ pinError }}
              </div>

              <button
                type="button"
                @click="handleVerifyPinAndRegister"
                :disabled="!enteredPin"
                :style="{ width: '100%', padding: '10px', background: enteredPin ? '#007A4D' : '#94a3b8', color: '#ffffff', border: 'none', borderRadius: '8px', fontSize: '13px', fontWeight: '700', cursor: enteredPin ? 'pointer' : 'not-allowed' }"
              >
                ✓ Verify PIN & Complete Sign Up
              </button>

              <div style="display: flex; justify-content: space-between; margin-top: 10px; font-size: 11px;">
                <button type="button" @click="enteredPin = generatedPin" style="background: transparent; border: none; color: #0078D4; font-weight: 700; cursor: pointer;">
                  ⚡ Autofill PIN ({{ generatedPin }})
                </button>
                <button type="button" @click="isWaitingForPin = false" style="background: transparent; border: none; color: #64748b; cursor: pointer;">
                  Edit Email
                </button>
              </div>
            </div>
          </div>

          <!-- 1-Tap Fast Demo Accounts -->
          <div style="margin-top: 16px; border-top: 1px solid #f1f5f9; padding-top: 12px;">
            <div style="font-size: 11px; font-weight: 600; color: #64748b; margin-bottom: 6px;">1-Tap Demo Student Login:</div>
            <div style="display: flex; gap: 6px;">
              <button
                type="button"
                @click="handleQuickLogin('n.khumalo@myuct.ac.za', 'UCT')"
                style="flex: 1; background: #f8fafc; border: 1px solid #cbd5e1; border-radius: 6px; padding: 5px; font-size: 11px; cursor: pointer; font-weight: 600;"
              >
                UCT
              </button>
              <button
                type="button"
                @click="handleQuickLogin('s.ndlovu@students.wits.ac.za', 'WITS')"
                style="flex: 1; background: #f8fafc; border: 1px solid #cbd5e1; border-radius: 6px; padding: 5px; font-size: 11px; cursor: pointer; font-weight: 600;"
              >
                Wits
              </button>
              <button
                type="button"
                @click="handleQuickLogin('a.vandermerwe@sun.ac.za', 'STELLENBOSCH')"
                style="flex: 1; background: #f8fafc; border: 1px solid #cbd5e1; border-radius: 6px; padding: 5px; font-size: 11px; cursor: pointer; font-weight: 600;"
              >
                Maties
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Realistic Microsoft Outlook Inbox Dialog Modal -->
    <div v-if="showOutlookModal" style="position: fixed; inset: 0; background: rgba(0,0,0,0.65); display: flex; align-items: center; justify-content: center; padding: 16px; z-index: 999;">
      <div style="background: #ffffff; border-radius: 16px; width: 100%; max-width: 540px; overflow: hidden; box-shadow: 0 25px 50px -12px rgba(0,0,0,0.5);">
        <!-- Outlook Blue Header -->
        <div style="background: #0078D4; color: #fff; padding: 14px 20px; display: flex; align-items: center; justify-content: space-between;">
          <div style="display: flex; align-items: center; gap: 10px;">
            <span style="font-size: 22px;">📬</span>
            <div>
              <div style="font-weight: 800; font-size: 15px;">Microsoft 365 Outlook</div>
              <div style="font-size: 11px; opacity: 0.9;">Student Webmail • Exchange Online</div>
            </div>
          </div>
          <button
            @click="showOutlookModal = false"
            style="background: transparent; border: none; color: #fff; font-size: 20px; cursor: pointer;"
          >
            ✕
          </button>
        </div>

        <!-- Email Content Box -->
        <div style="padding: 20px;">
          <div style="background: #f8fafc; border: 1px solid #e2e8f0; border-radius: 10px; padding: 12px; margin-bottom: 14px; font-size: 12px;">
            <div style="color: #64748b;">From: <strong style="color: #0f172a;">verification@communitystore.ac.za</strong></div>
            <div style="color: #64748b;">To: <strong style="color: #0f172a;">{{ loginEmail }}</strong></div>
            <div style="color: #64748b; margin-top: 4px;">Subject: <strong style="color: #0078D4;">🔐 Your Community Store Verification PIN: {{ generatedPin }}</strong></div>
          </div>

          <div style="font-size: 13px; color: #334155; line-height: 1.5; margin-bottom: 16px;">
            <p>Dumelang / Molo / Hello {{ loginName || 'Student' }},</p>
            <p>Welcome to the <strong>Community Store Campus Marketplace</strong>. Use the 6-digit one-time security PIN below to complete your registration:</p>

            <div style="background: #e0f2fe; border: 2px dashed #0284c7; border-radius: 12px; padding: 16px; text-align: center; margin: 14px 0;">
              <div style="font-size: 11px; font-weight: 700; color: #0369a1; text-transform: uppercase; letter-spacing: 1px;">Your One-Time PIN</div>
              <div style="font-size: 32px; font-weight: 900; color: #0369a1; letter-spacing: 6px; margin: 6px 0;">{{ generatedPin }}</div>
              <div style="font-size: 10px; color: #0284c7;">Valid for 10 minutes • Keep this PIN confidential</div>
            </div>

            <p style="font-size: 11px; color: #64748b;">If you did not request this verification code, please ignore this email.</p>
          </div>

          <div style="display: flex; gap: 10px; justify-content: flex-end;">
            <button
              @click="showOutlookModal = false"
              style="padding: 8px 14px; background: #f1f5f9; border: 1px solid #cbd5e1; border-radius: 8px; font-size: 12px; cursor: pointer; font-weight: 600;"
            >
              Close
            </button>
            <button
              @click="enteredPin = generatedPin; showOutlookModal = false;"
              style="padding: 8px 16px; background: #0078D4; color: #fff; border: none; border-radius: 8px; font-size: 12px; cursor: pointer; font-weight: 700;"
            >
              ⚡ Autofill PIN into Verification Box
            </button>
          </div>
        </div>
      </div>
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
      <!-- Hero Banner -->
      <div v-if="activeTab === 'store'" style="background: linear-gradient(rgba(0,0,0,0.55), rgba(0,0,0,0.85)), url(https://images.unsplash.com/photo-1544025162-d76694265947?auto=format&fit=crop&w=1200&q=80); background-size: cover; background-position: center; color: #fff; border-radius: 20px; padding: 40px; margin-bottom: 24px;">
        <span style="background: #007A4D; padding: 4px 12px; border-radius: 6px; font-size: 12px; font-weight: 700;">🇿🇦 MZANSI CAMPUS STORE • VUE 3</span>
        <h2 style="font-size: 28px; font-weight: 800; margin-top: 12px; margin-bottom: 8px;">Trusted South African Student & Local Marketplace</h2>
        <p style="font-size: 14px; max-width: 640px; opacity: 0.9;">
          Exchange textbooks, electronics, dorm essentials, Karoo biltong, and local farm goods securely with verified university students and vendors.
        </p>
      </div>

      <!-- Filters -->
      <div v-if="activeTab === 'store'" style="display: flex; flex-wrap: wrap; gap: 12px; align-items: center; justify-content: space-between; margin-bottom: 24px; background: #fff; padding: 16px; border-radius: 16px; border: 1px solid #e2e8f0;">
        <input
          type="text"
          v-model="searchQuery"
          placeholder="Search textbooks, dorm tech, Karoo biltong, farm produce..."
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
  },
  {
    id: 'prod-4',
    title: 'Karoo Traditional Beef Biltong & Droëwors Craft Pack (500g)',
    desc: 'Freshly cured spiced traditional beef biltong. High protein student study snack.',
    price: 165,
    origPrice: 240,
    category: 'Local Produce & Food',
    campus: 'UCT',
    condition: 'Fresh / New',
    seller: 'Karoo Heritage Meats',
    safeSpot: 'Leslie Social Science Plaza Stalls',
    ecoKg: 2.5,
    img: 'https://images.unsplash.com/photo-1544025162-d76694265947?auto=format&fit=crop&w=600&q=80'
  }
]

const user = ref<{ email: string; name: string } | null>(null)
const isSignUp = ref(false)
const isWaitingForPin = ref(false)
const enteredPin = ref('')
const generatedPin = '849201'
const outlookNotification = ref('')
const showOutlookModal = ref(false)
const pinError = ref('')
const deviceMode = ref<'auto' | 'laptop' | 'phone'>('auto')
const selectedMarketPic = ref(0)

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
]

const currentPic = computed(() => MARKET_PICTURES[selectedMarketPic.value])

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

const handleDirectLogin = () => {
  user.value = {
    email: loginEmail.value,
    name: loginName.value || 'Verified Student'
  }
}

const handleQuickLogin = (email: string, campus: string) => {
  loginEmail.value = email
  user.value = {
    email,
    name: email.includes('khumalo') ? 'Nandi Khumalo' : email.includes('ndlovu') ? 'Sipho Ndlovu' : 'Anika van der Merwe'
  }
}

const handleSendOutlookPin = () => {
  isWaitingForPin.value = true
  pinError.value = ''
  outlookNotification.value = `📧 Microsoft Outlook Webmail: [Community Store] Verification PIN is: ${generatedPin} (Sent to ${loginEmail.value})`
  showOutlookModal.value = true
}

const handleVerifyPinAndRegister = () => {
  if (enteredPin.value.trim() === generatedPin) {
    user.value = {
      email: loginEmail.value,
      name: loginName.value || 'Verified Student'
    }
    outlookNotification.value = ''
    showOutlookModal.value = false
    pinError.value = ''
  } else {
    pinError.value = `Incorrect PIN "${enteredPin.value}". Please check your Outlook inbox.`
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
