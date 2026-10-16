const fs = require('fs');
const path = require('path');
const sharp = require('/tmp/node_modules/sharp');

const outDir = path.join(__dirname, 'play_store_assets');
if (!fs.existsSync(outDir)) {
  fs.mkdirSync(outDir, { recursive: true });
}

// Helper for phone SVG mockup
function getPhoneMockup(contentSvg, isDark = false) {
  const bg = isDark ? '#0B0F19' : '#FFFFFF';
  const border = isDark ? '#1F293D' : '#E2E8F0';
  const statusColor = isDark ? '#94A3B8' : '#64748B';
  return `
    <g transform="translate(180, 480)">
      <!-- Phone Shadow -->
      <rect x="15" y="25" width="720" height="1360" rx="64" fill="#000000" opacity="0.35" filter="url(#phoneShadow)" />
      
      <!-- Phone Outer Bezel -->
      <rect x="0" y="0" width="720" height="1360" rx="60" fill="#1E293B" stroke="#475569" stroke-width="6" />
      
      <!-- Phone Inner Screen -->
      <rect x="16" y="16" width="688" height="1328" rx="48" fill="${bg}" />
      
      <!-- Dynamic Island / Speaker Pill -->
      <rect x="250" y="32" width="220" height="36" rx="18" fill="#0B0F19" />
      <circle cx="430" cy="50" r="6" fill="#1E293B" />
      
      <!-- Status Bar -->
      <text x="56" y="56" font-family="'Inter', 'Segoe UI', Roboto, sans-serif" font-size="20" font-weight="700" fill="${statusColor}">9:41</text>
      <g transform="translate(610, 40)">
        <!-- Battery Icon -->
        <rect x="0" y="2" width="28" height="16" rx="4" fill="none" stroke="${statusColor}" stroke-width="2" />
        <rect x="3" y="5" width="18" height="10" rx="2" fill="${statusColor}" />
        <rect x="29" y="7" width="2" height="6" fill="${statusColor}" />
      </g>
      
      <!-- Screen Content Injected -->
      <g transform="translate(16, 85)">
        <clipPath id="screenClip">
          <rect x="0" y="0" width="688" height="1255" rx="36" />
        </clipPath>
        <g clip-path="url(#screenClip)">
          ${contentSvg}
        </g>
      </g>
    </g>
  `;
}

// 1. Feature Graphic (1024 x 500)
function getFeatureGraphicSvg() {
  return `
<svg width="1024" height="500" viewBox="0 0 1024 500" xmlns="http://www.w3.org/2000/svg">
  <defs>
    <linearGradient id="bgGrad" x1="0%" y1="0%" x2="100%" y2="100%">
      <stop offset="0%" stop-color="#0F172A" />
      <stop offset="50%" stop-color="#1E1B4B" />
      <stop offset="100%" stop-color="#312E81" />
    </linearGradient>
    <linearGradient id="accentGrad" x1="0%" y1="0%" x2="100%" y2="0%">
      <stop offset="0%" stop-color="#6366F1" />
      <stop offset="50%" stop-color="#8B5CF6" />
      <stop offset="100%" stop-color="#EC4899" />
    </linearGradient>
    <linearGradient id="cardGrad" x1="0%" y1="0%" x2="100%" y2="100%">
      <stop offset="0%" stop-color="#FFFFFF" stop-opacity="0.12" />
      <stop offset="100%" stop-color="#FFFFFF" stop-opacity="0.04" />
    </linearGradient>
    <filter id="glow" x="-20%" y="-20%" width="140%" height="140%">
      <feGaussianBlur stdDeviation="40" result="blur" />
      <feComposite in="SourceGraphic" in2="blur" operator="over" />
    </filter>
    <filter id="cardShadow" x="-20%" y="-20%" width="140%" height="140%">
      <feDropShadow dx="0" dy="12" stdDeviation="16" flood-color="#000000" flood-opacity="0.4" />
    </filter>
  </defs>

  <!-- Background Canvas -->
  <rect width="1024" height="500" fill="url(#bgGrad)" />
  
  <!-- Glowing Background Orbs -->
  <circle cx="850" cy="180" r="220" fill="#6366F1" opacity="0.35" filter="url(#glow)" />
  <circle cx="950" cy="380" r="180" fill="#EC4899" opacity="0.25" filter="url(#glow)" />
  <circle cx="200" cy="450" r="160" fill="#4338CA" opacity="0.3" filter="url(#glow)" />

  <!-- Left Content Area -->
  <g transform="translate(60, 75)">
    <!-- Category Pill -->
    <rect x="0" y="0" width="220" height="36" rx="18" fill="#4F46E5" opacity="0.3" stroke="#818CF8" stroke-width="1.5" />
    <circle cx="20" cy="18" r="5" fill="#34D399" />
    <text x="35" y="24" font-family="'Inter', Roboto, sans-serif" font-size="13" font-weight="700" fill="#E0E7FF" letter-spacing="1">STUDENT ORGANIZER</text>

    <!-- Main Title -->
    <text x="0" y="110" font-family="'Inter', Roboto, sans-serif" font-size="64" font-weight="900" fill="#FFFFFF" letter-spacing="-1">Class<tspan fill="url(#accentGrad)">Mate</tspan></text>
    
    <!-- Subtitle -->
    <text x="0" y="155" font-family="'Inter', Roboto, sans-serif" font-size="20" font-weight="500" fill="#CBD5E1">Your All-In-One Academic Planner &amp; Study Hub</text>

    <!-- Bullet Features Grid -->
    <g transform="translate(0, 195)">
      <!-- Item 1 -->
      <rect x="0" y="0" width="180" height="42" rx="12" fill="url(#cardGrad)" stroke="#6366F1" stroke-width="1" />
      <text x="18" y="26" font-family="'Inter', Roboto, sans-serif" font-size="14" font-weight="600" fill="#FFFFFF">📅 Smart Timetable</text>

      <!-- Item 2 -->
      <rect x="195" y="0" width="180" height="42" rx="12" fill="url(#cardGrad)" stroke="#8B5CF6" stroke-width="1" />
      <text x="213" y="26" font-family="'Inter', Roboto, sans-serif" font-size="14" font-weight="600" fill="#FFFFFF">📝 Homework Tracker</text>

      <!-- Item 3 -->
      <rect x="0" y="54" width="180" height="42" rx="12" fill="url(#cardGrad)" stroke="#EC4899" stroke-width="1" />
      <text x="18" y="80" font-family="'Inter', Roboto, sans-serif" font-size="14" font-weight="600" fill="#FFFFFF">📊 GPA Calculator</text>

      <!-- Item 4 -->
      <rect x="195" y="54" width="180" height="42" rx="12" fill="url(#cardGrad)" stroke="#10B981" stroke-width="1" />
      <text x="213" y="80" font-family="'Inter', Roboto, sans-serif" font-size="14" font-weight="600" fill="#FFFFFF">🔔 Exam Alerts</text>
    </g>

    <!-- Bottom Badge -->
    <g transform="translate(0, 320)">
      <text x="0" y="22" font-family="'Inter', Roboto, sans-serif" font-size="14" font-weight="700" fill="#FBBF24">★★★★★</text>
      <text x="70" y="22" font-family="'Inter', Roboto, sans-serif" font-size="14" font-weight="600" fill="#94A3B8">Trusted by University &amp; School Students Worldwide</text>
    </g>
  </g>

  <!-- Right Visual Mockup (Floating 3D Phone + Floating Cards) -->
  <g transform="translate(680, 50)" filter="url(#cardShadow)">
    <!-- Phone Body -->
    <rect x="0" y="0" width="280" height="460" rx="36" fill="#0F172A" stroke="#334155" stroke-width="4" />
    <rect x="10" y="10" width="260" height="440" rx="28" fill="#1E293B" />
    
    <!-- Phone Notch -->
    <rect x="90" y="18" width="100" height="18" rx="9" fill="#0B0F19" />
    
    <!-- Phone Screen Content (Dashboard Preview) -->
    <g transform="translate(24, 50)">
      <text x="0" y="20" font-family="'Inter', Roboto, sans-serif" font-size="13" font-weight="700" fill="#94A3B8">TODAY'S SCHEDULE</text>
      
      <!-- Class 1 -->
      <rect x="0" y="32" width="232" height="64" rx="14" fill="#312E81" stroke="#4F46E5" stroke-width="1" />
      <text x="14" y="58" font-family="'Inter', Roboto, sans-serif" font-size="14" font-weight="700" fill="#FFFFFF">Physics &amp; Mechanics</text>
      <text x="14" y="78" font-family="'Inter', Roboto, sans-serif" font-size="11" font-weight="500" fill="#A5B4FC">10:00 AM • Room 402</text>
      
      <!-- Class 2 -->
      <rect x="0" y="106" width="232" height="64" rx="14" fill="#064E3B" stroke="#059669" stroke-width="1" />
      <text x="14" y="132" font-family="'Inter', Roboto, sans-serif" font-size="14" font-weight="700" fill="#FFFFFF">Data Structures Lab</text>
      <text x="14" y="152" font-family="'Inter', Roboto, sans-serif" font-size="11" font-weight="500" fill="#6EE7B7">01:30 PM • Lab 3</text>

      <!-- Mini GPA Card -->
      <rect x="0" y="180" width="232" height="85" rx="14" fill="#1E1B4B" stroke="#8B5CF6" stroke-width="1" />
      <text x="14" y="205" font-family="'Inter', Roboto, sans-serif" font-size="12" font-weight="700" fill="#C4B5FD">SEMESTER GPA</text>
      <text x="14" y="245" font-family="'Inter', Roboto, sans-serif" font-size="32" font-weight="900" fill="#FFFFFF">3.92<tspan font-size="14" fill="#A78BFA" font-weight="500"> / 4.00</tspan></text>
      <rect x="150" y="200" width="65" height="45" rx="10" fill="#059669" />
      <text x="182" y="228" font-family="'Inter', Roboto, sans-serif" font-size="16" font-weight="800" fill="#FFFFFF" text-anchor="middle">A+</text>
    </g>
  </g>

  <!-- Floating Badge 1 (Left of Phone) -->
  <g transform="translate(540, 160)" filter="url(#cardShadow)">
    <rect x="0" y="0" width="160" height="50" rx="16" fill="#1E293B" stroke="#6366F1" stroke-width="2" />
    <text x="16" y="24" font-family="'Inter', Roboto, sans-serif" font-size="12" font-weight="700" fill="#A5B4FC">NEXT ASSIGNMENT</text>
    <text x="16" y="40" font-family="'Inter', Roboto, sans-serif" font-size="11" font-weight="600" fill="#F87171">⏳ Due in 4 hours</text>
  </g>

  <!-- Floating Badge 2 (Bottom of Phone) -->
  <g transform="translate(590, 350)" filter="url(#cardShadow)">
    <rect x="0" y="0" width="170" height="50" rx="16" fill="#1E293B" stroke="#10B981" stroke-width="2" />
    <text x="16" y="24" font-family="'Inter', Roboto, sans-serif" font-size="12" font-weight="700" fill="#6EE7B7">ATTENDANCE</text>
    <text x="16" y="40" font-family="'Inter', Roboto, sans-serif" font-size="12" font-weight="800" fill="#FFFFFF">94% Safe to Bunk ✓</text>
  </g>
</svg>
`;
}

// 2. High Res App Icon (512 x 512)
function getAppIconSvg() {
  return `
<svg width="512" height="512" viewBox="0 0 512 512" xmlns="http://www.w3.org/2000/svg">
  <defs>
    <linearGradient id="iconBg" x1="0%" y1="0%" x2="100%" y2="100%">
      <stop offset="0%" stop-color="#4F46E5" />
      <stop offset="50%" stop-color="#6366F1" />
      <stop offset="100%" stop-color="#8B5CF6" />
    </linearGradient>
    <linearGradient id="bookGrad" x1="0%" y1="0%" x2="100%" y2="100%">
      <stop offset="0%" stop-color="#FFFFFF" />
      <stop offset="100%" stop-color="#E0E7FF" />
    </linearGradient>
    <filter id="iconShadow" x="-20%" y="-20%" width="140%" height="140%">
      <feDropShadow dx="0" dy="16" stdDeviation="20" flood-color="#1E1B4B" flood-opacity="0.5" />
    </filter>
  </defs>

  <!-- Rounded Squircle Icon Base -->
  <rect x="16" y="16" width="480" height="480" rx="112" fill="url(#iconBg)" filter="url(#iconShadow)" />
  
  <!-- Subtle Inner Border Highlight -->
  <rect x="18" y="18" width="476" height="476" rx="110" fill="none" stroke="#FFFFFF" stroke-opacity="0.25" stroke-width="4" />

  <!-- Center Graphic: Graduation Cap & Academic Calendar Symbol -->
  <g transform="translate(106, 110)">
    <!-- Calendar Base Sheet -->
    <rect x="40" y="80" width="220" height="210" rx="28" fill="url(#bookGrad)" />
    <rect x="40" y="80" width="220" height="55" rx="28" fill="#312E81" />
    <rect x="40" y="110" width="220" height="25" fill="#312E81" />
    
    <!-- Calendar Rings -->
    <rect x="85" y="65" width="14" height="32" rx="7" fill="#F8FAFC" />
    <rect x="145" y="65" width="14" height="32" rx="7" fill="#F8FAFC" />
    <rect x="205" y="65" width="14" height="32" rx="7" fill="#F8FAFC" />

    <!-- Calendar Grid Dots -->
    <circle cx="85" cy="170" r="12" fill="#6366F1" />
    <circle cx="150" cy="170" r="12" fill="#6366F1" />
    <circle cx="215" cy="170" r="12" fill="#10B981" />
    <circle cx="85" cy="225" r="12" fill="#8B5CF6" />
    <circle cx="150" cy="225" r="12" fill="#EC4899" />
    <circle cx="215" cy="225" r="12" fill="#F59E0B" />

    <!-- Graduation Cap Overlay (3D Angled) -->
    <g transform="translate(10, 0)">
      <!-- Cap Diamond -->
      <polygon points="140,10 270,60 140,105 10,60" fill="#1E1B4B" stroke="#4F46E5" stroke-width="4" />
      <polygon points="140,18 250,60 140,98 30,60" fill="#312E81" />
      <!-- Cap Skullcap -->
      <path d="M 60,65 Q 140,125 220,65 L 220,95 Q 140,150 60,95 Z" fill="#1E1B4B" />
      <!-- Tassel -->
      <circle cx="140" cy="58" r="8" fill="#F59E0B" />
      <path d="M 140,58 Q 230,70 245,120" fill="none" stroke="#F59E0B" stroke-width="6" stroke-linecap="round" />
      <rect x="238" y="115" width="14" height="28" rx="6" fill="#F59E0B" />
    </g>
  </g>
</svg>
`;
}

// 8 Screenshots Builder (1080 x 1920)
function getScreenshotSvg(index, title, subtitle, badgeText, badgeColor, screenContent, isDark = false) {
  const bgGrad1 = isDark ? '#0B0F19' : '#EEF2FF';
  const bgGrad2 = isDark ? '#1E1B4B' : '#E0E7FF';
  const textTitleColor = isDark ? '#FFFFFF' : '#1E1B4B';
  const textSubColor = isDark ? '#94A3B8' : '#475569';

  return `
<svg width="1080" height="1920" viewBox="0 0 1080 1920" xmlns="http://www.w3.org/2000/svg">
  <defs>
    <linearGradient id="scBg${index}" x1="0%" y1="0%" x2="0%" y2="100%">
      <stop offset="0%" stop-color="${bgGrad1}" />
      <stop offset="100%" stop-color="${bgGrad2}" />
    </linearGradient>
    <filter id="phoneShadow" x="-20%" y="-20%" width="140%" height="140%">
      <feDropShadow dx="0" dy="24" stdDeviation="36" flood-color="#000000" flood-opacity="0.3" />
    </filter>
    <filter id="badgeShadow" x="-20%" y="-20%" width="140%" height="140%">
      <feDropShadow dx="0" dy="8" stdDeviation="14" flood-color="#000000" flood-opacity="0.25" />
    </filter>
  </defs>

  <!-- Background -->
  <rect width="1080" height="1920" fill="url(#scBg${index})" />

  <!-- Top Marketing Headers -->
  <g transform="translate(540, 150)" text-anchor="middle">
    <!-- Super Title Pill -->
    <rect x="-130" y="0" width="260" height="48" rx="24" fill="${badgeColor}" opacity="0.18" />
    <text x="0" y="32" font-family="'Inter', Roboto, sans-serif" font-size="16" font-weight="800" fill="${badgeColor}" letter-spacing="2">${badgeText.toUpperCase()}</text>

    <!-- Headline -->
    <text x="0" y="130" font-family="'Inter', Roboto, sans-serif" font-size="56" font-weight="900" fill="${textTitleColor}" letter-spacing="-1">${title}</text>
    
    <!-- Subtitle -->
    <text x="0" y="195" font-family="'Inter', Roboto, sans-serif" font-size="26" font-weight="500" fill="${textSubColor}">${subtitle}</text>
  </g>

  <!-- Injected Smartphone Frame & UI -->
  ${getPhoneMockup(screenContent, isDark)}

  <!-- Floating Feature Pill Accent -->
  <g transform="translate(540, 1780)" text-anchor="middle" filter="url(#badgeShadow)">
    <rect x="-240" y="0" width="480" height="64" rx="32" fill="#1E293B" stroke="${badgeColor}" stroke-width="2" />
    <text x="0" y="40" font-family="'Inter', Roboto, sans-serif" font-size="22" font-weight="700" fill="#FFFFFF">✨ ClassMate • Top Student Choice</text>
  </g>
</svg>
`;
}

// Content 1: Weekly Timetable
const screen1 = `
  <rect width="688" height="1255" fill="#F8FAFC" />
  <g transform="translate(36, 20)">
    <text x="0" y="36" font-family="sans-serif" font-size="28" font-weight="900" fill="#0F172A">Weekly Timetable</text>
    <text x="0" y="66" font-family="sans-serif" font-size="16" font-weight="600" fill="#64748B">Fall Semester 2026</text>
    
    <!-- Days of Week Pills -->
    <g transform="translate(0, 95)">
      <rect x="0" y="0" width="110" height="46" rx="23" fill="#4F46E5" />
      <text x="55" y="29" font-family="sans-serif" font-size="16" font-weight="700" fill="#FFFFFF" text-anchor="middle">Mon</text>
      
      <rect x="125" y="0" width="110" height="46" rx="23" fill="#E2E8F0" />
      <text x="180" y="29" font-family="sans-serif" font-size="16" font-weight="700" fill="#475569" text-anchor="middle">Tue</text>
      
      <rect x="250" y="0" width="110" height="46" rx="23" fill="#E2E8F0" />
      <text x="305" y="29" font-family="sans-serif" font-size="16" font-weight="700" fill="#475569" text-anchor="middle">Wed</text>
      
      <rect x="375" y="0" width="110" height="46" rx="23" fill="#E2E8F0" />
      <text x="430" y="29" font-family="sans-serif" font-size="16" font-weight="700" fill="#475569" text-anchor="middle">Thu</text>
      
      <rect x="500" y="0" width="110" height="46" rx="23" fill="#E2E8F0" />
      <text x="555" y="29" font-family="sans-serif" font-size="16" font-weight="700" fill="#475569" text-anchor="middle">Fri</text>
    </g>

    <!-- Routine Cards -->
    <g transform="translate(0, 175)">
      <!-- Card 1 -->
      <rect x="0" y="0" width="616" height="150" rx="24" fill="#EEF2FF" stroke="#C7D2FE" stroke-width="2" />
      <rect x="0" y="0" width="14" height="150" rx="6" fill="#4F46E5" />
      <text x="36" y="44" font-family="sans-serif" font-size="22" font-weight="800" fill="#1E1B4B">Computer Science 101</text>
      <text x="36" y="78" font-family="sans-serif" font-size="16" font-weight="600" fill="#4F46E5">09:00 AM – 10:30 AM</text>
      <text x="36" y="115" font-family="sans-serif" font-size="15" font-weight="500" fill="#64748B">📍 Lab Hall 4 • Prof. Anderson</text>

      <!-- Card 2 -->
      <rect x="0" y="180" width="616" height="150" rx="24" fill="#F0FDF4" stroke="#BBF7D0" stroke-width="2" />
      <rect x="0" y="0" width="14" height="150" rx="6" fill="#10B981" />
      <text x="36" y="224" font-family="sans-serif" font-size="22" font-weight="800" fill="#064E3B">Calculus &amp; Linear Algebra</text>
      <text x="36" y="258" font-family="sans-serif" font-size="16" font-weight="600" fill="#059669">11:00 AM – 12:30 PM</text>
      <text x="36" y="295" font-family="sans-serif" font-size="15" font-weight="500" fill="#64748B">📍 Lecture Theater 2 • Dr. Watson</text>

      <!-- Card 3 -->
      <rect x="0" y="360" width="616" height="150" rx="24" fill="#FEF3C7" stroke="#FDE68A" stroke-width="2" />
      <rect x="0" y="0" width="14" height="150" rx="6" fill="#F59E0B" />
      <text x="36" y="404" font-family="sans-serif" font-size="22" font-weight="800" fill="#78350F">Physics &amp; Thermodynamics</text>
      <text x="36" y="438" font-family="sans-serif" font-size="16" font-weight="600" fill="#D97706">02:00 PM – 03:30 PM</text>
      <text x="36" y="475" font-family="sans-serif" font-size="15" font-weight="500" fill="#64748B">📍 Room 204 • Prof. Clark</text>

      <!-- Card 4 -->
      <rect x="0" y="540" width="616" height="150" rx="24" fill="#FDF2F8" stroke="#FBCFE8" stroke-width="2" />
      <rect x="0" y="0" width="14" height="150" rx="6" fill="#EC4899" />
      <text x="36" y="584" font-family="sans-serif" font-size="22" font-weight="800" fill="#831843">English Literature Seminar</text>
      <text x="36" y="618" font-family="sans-serif" font-size="16" font-weight="600" fill="#DB2777">04:00 PM – 05:00 PM</text>
      <text x="36" y="655" font-family="sans-serif" font-size="15" font-weight="500" fill="#64748B">📍 Room 108 • Prof. Eleanor</text>
    </g>
  </g>
`;

// Content 2: Homework & Assignments
const screen2 = `
  <rect width="688" height="1255" fill="#F8FAFC" />
  <g transform="translate(36, 20)">
    <text x="0" y="36" font-family="sans-serif" font-size="28" font-weight="900" fill="#0F172A">Assignments &amp; Tasks</text>
    <text x="0" y="66" font-family="sans-serif" font-size="16" font-weight="600" fill="#64748B">5 Pending • 2 Due This Week</text>

    <!-- Task Cards -->
    <g transform="translate(0, 105)">
      <!-- Task 1 -->
      <rect x="0" y="0" width="616" height="135" rx="22" fill="#FFFFFF" stroke="#E2E8F0" stroke-width="2" />
      <circle cx="48" cy="67" r="18" fill="none" stroke="#4F46E5" stroke-width="3" />
      <text x="86" y="55" font-family="sans-serif" font-size="20" font-weight="800" fill="#1E293B">Database Project Phase 2</text>
      <text x="86" y="88" font-family="sans-serif" font-size="14" font-weight="600" fill="#EF4444">🔥 Due Tomorrow at 11:59 PM</text>
      <rect x="490" y="47" width="100" height="38" rx="10" fill="#FEE2E2" />
      <text x="540" y="72" font-family="sans-serif" font-size="13" font-weight="800" fill="#DC2626" text-anchor="middle">HIGH</text>

      <!-- Task 2 -->
      <rect x="0" y="160" width="616" height="135" rx="22" fill="#FFFFFF" stroke="#E2E8F0" stroke-width="2" />
      <circle cx="48" cy="227" r="18" fill="none" stroke="#10B981" stroke-width="3" />
      <text x="86" y="215" font-family="sans-serif" font-size="20" font-weight="800" fill="#1E293B">Calculus Problem Set #4</text>
      <text x="86" y="248" font-family="sans-serif" font-size="14" font-weight="600" fill="#F59E0B">⏳ Due in 3 days</text>
      <rect x="490" y="207" width="100" height="38" rx="10" fill="#FEF3C7" />
      <text x="540" y="232" font-family="sans-serif" font-size="13" font-weight="800" fill="#D97706" text-anchor="middle">MEDIUM</text>

      <!-- Task 3 -->
      <rect x="0" y="320" width="616" height="135" rx="22" fill="#FFFFFF" stroke="#E2E8F0" stroke-width="2" />
      <circle cx="48" cy="387" r="18" fill="none" stroke="#8B5CF6" stroke-width="3" />
      <text x="86" y="375" font-family="sans-serif" font-size="20" font-weight="800" fill="#1E293B">Physics Lab Report: Optics</text>
      <text x="86" y="408" font-family="sans-serif" font-size="14" font-weight="600" fill="#64748B">📅 Due next Monday</text>
      <rect x="490" y="367" width="100" height="38" rx="10" fill="#E0E7FF" />
      <text x="540" y="392" font-family="sans-serif" font-size="13" font-weight="800" fill="#4F46E5" text-anchor="middle">NORMAL</text>

      <!-- Completed Task -->
      <rect x="0" y="480" width="616" height="110" rx="22" fill="#F8FAFC" stroke="#E2E8F0" stroke-width="1.5" />
      <circle cx="48" cy="535" r="18" fill="#10B981" />
      <text x="48" y="542" font-family="sans-serif" font-size="18" font-weight="900" fill="#FFFFFF" text-anchor="middle">✓</text>
      <text x="86" y="535" font-family="sans-serif" font-size="18" font-weight="600" fill="#94A3B8" text-decoration="line-through">Algorithm Analysis Quiz 1</text>
      <text x="86" y="562" font-family="sans-serif" font-size="13" font-weight="500" fill="#10B981">Completed on Oct 14</text>
    </g>
  </g>
`;

// Content 3: GPA Calculator
const screen3 = `
  <rect width="688" height="1255" fill="#F8FAFC" />
  <g transform="translate(36, 20)">
    <text x="0" y="36" font-family="sans-serif" font-size="28" font-weight="900" fill="#0F172A">GPA &amp; Grade Tracker</text>
    <text x="0" y="66" font-family="sans-serif" font-size="16" font-weight="600" fill="#64748B">Academic Performance Overview</text>

    <!-- GPA Hero Card -->
    <rect x="0" y="95" width="616" height="230" rx="28" fill="#312E81" />
    <text x="36" y="145" font-family="sans-serif" font-size="18" font-weight="700" fill="#A5B4FC">CUMULATIVE GPA</text>
    <text x="36" y="225" font-family="sans-serif" font-size="64" font-weight="900" fill="#FFFFFF">3.92</text>
    <text x="180" y="210" font-family="sans-serif" font-size="22" font-weight="600" fill="#C7D2FE">/ 4.00</text>
    <rect x="420" y="130" width="150" height="130" rx="20" fill="#4F46E5" />
    <text x="495" y="185" font-family="sans-serif" font-size="44" font-weight="900" fill="#FFFFFF" text-anchor="middle">A</text>
    <text x="495" y="225" font-family="sans-serif" font-size="14" font-weight="700" fill="#A5B4FC" text-anchor="middle">EXCELLENT</text>

    <!-- Semester Course Breakdown -->
    <text x="0" y="375" font-family="sans-serif" font-size="22" font-weight="800" fill="#1E293B">Courses This Semester</text>
    
    <g transform="translate(0, 400)">
      <!-- Course 1 -->
      <rect x="0" y="0" width="616" height="90" rx="18" fill="#FFFFFF" stroke="#E2E8F0" stroke-width="2" />
      <text x="24" y="42" font-family="sans-serif" font-size="18" font-weight="800" fill="#1E293B">Software Engineering</text>
      <text x="24" y="68" font-family="sans-serif" font-size="14" font-weight="500" fill="#64748B">3.0 Credits • Grade: 96%</text>
      <rect x="520" y="22" width="65" height="46" rx="12" fill="#D1FAE5" />
      <text x="552" y="52" font-family="sans-serif" font-size="18" font-weight="800" fill="#065F46" text-anchor="middle">4.0</text>

      <!-- Course 2 -->
      <rect x="0" y="110" width="616" height="90" rx="18" fill="#FFFFFF" stroke="#E2E8F0" stroke-width="2" />
      <text x="24" y="152" font-family="sans-serif" font-size="18" font-weight="800" fill="#1E293B">Artificial Intelligence</text>
      <text x="24" y="178" font-family="sans-serif" font-size="14" font-weight="500" fill="#64748B">3.0 Credits • Grade: 92%</text>
      <rect x="520" y="132" width="65" height="46" rx="12" fill="#D1FAE5" />
      <text x="552" y="162" font-family="sans-serif" font-size="18" font-weight="800" fill="#065F46" text-anchor="middle">3.9</text>

      <!-- Course 3 -->
      <rect x="0" y="220" width="616" height="90" rx="18" fill="#FFFFFF" stroke="#E2E8F0" stroke-width="2" />
      <text x="24" y="262" font-family="sans-serif" font-size="18" font-weight="800" fill="#1E293B">Cyber Security Fundamentals</text>
      <text x="24" y="288" font-family="sans-serif" font-size="14" font-weight="500" fill="#64748B">3.0 Credits • Grade: 89%</text>
      <rect x="520" y="242" width="65" height="46" rx="12" fill="#FEF3C7" />
      <text x="552" y="272" font-family="sans-serif" font-size="18" font-weight="800" fill="#92400E" text-anchor="middle">3.7</text>
    </g>
  </g>
`;

// Content 4: Smart Notes
const screen4 = `
  <rect width="688" height="1255" fill="#F8FAFC" />
  <g transform="translate(36, 20)">
    <text x="0" y="36" font-family="sans-serif" font-size="28" font-weight="900" fill="#0F172A">Lecture Notes</text>
    <text x="0" y="66" font-family="sans-serif" font-size="16" font-weight="600" fill="#64748B">Fast Search, Tags &amp; Sharing</text>

    <!-- Search Bar -->
    <rect x="0" y="95" width="616" height="56" rx="18" fill="#FFFFFF" stroke="#CBD5E1" stroke-width="2" />
    <text x="24" y="130" font-family="sans-serif" font-size="16" font-weight="500" fill="#94A3B8">🔍 Search lecture notes, formulas...</text>

    <!-- Note Cards -->
    <g transform="translate(0, 180)">
      <!-- Note 1 -->
      <rect x="0" y="0" width="616" height="160" rx="22" fill="#FFFFFF" stroke="#E2E8F0" stroke-width="2" />
      <rect x="24" y="24" width="90" height="30" rx="8" fill="#EEF2FF" />
      <text x="69" y="44" font-family="sans-serif" font-size="12" font-weight="700" fill="#4F46E5" text-anchor="middle">PHYSICS</text>
      <text x="24" y="85" font-family="sans-serif" font-size="20" font-weight="800" fill="#1E293B">Maxwell's Equations Summary</text>
      <text x="24" y="115" font-family="sans-serif" font-size="14" font-weight="500" fill="#64748B">Key formulas for electromagnetic induction and magnetic...</text>
      <text x="24" y="142" font-family="sans-serif" font-size="12" font-weight="600" fill="#94A3B8">Updated Yesterday • 2 Attachments</text>

      <!-- Note 2 -->
      <rect x="0" y="190" width="616" height="160" rx="22" fill="#FFFFFF" stroke="#E2E8F0" stroke-width="2" />
      <rect x="24" y="214" width="90" height="30" rx="8" fill="#ECFDF5" />
      <text x="69" y="234" font-family="sans-serif" font-size="12" font-weight="700" fill="#059669" text-anchor="middle">BIOLOGY</text>
      <text x="24" y="275" font-family="sans-serif" font-size="20" font-weight="800" fill="#1E293B">Cellular Respiration Cycle</text>
      <text x="24" y="305" font-family="sans-serif" font-size="14" font-weight="500" fill="#64748B">Glycolysis, Krebs cycle, and ATP synthesis mechanisms...</text>
      <text x="24" y="332" font-family="sans-serif" font-size="12" font-weight="600" fill="#94A3B8">Updated 3 days ago • Shared</text>
    </g>
  </g>
`;

// Content 5: Attendance Tracker
const screen5 = `
  <rect width="688" height="1255" fill="#F8FAFC" />
  <g transform="translate(36, 20)">
    <text x="0" y="36" font-family="sans-serif" font-size="28" font-weight="900" fill="#0F172A">Attendance Tracker</text>
    <text x="0" y="66" font-family="sans-serif" font-size="16" font-weight="600" fill="#64748B">Required Minimum: 75% Criteria</text>

    <!-- Overall Attendance Card -->
    <rect x="0" y="95" width="616" height="180" rx="24" fill="#065F46" />
    <text x="36" y="140" font-family="sans-serif" font-size="16" font-weight="700" fill="#A7F3D0">OVERALL ATTENDANCE</text>
    <text x="36" y="210" font-family="sans-serif" font-size="54" font-weight="900" fill="#FFFFFF">89.4%</text>
    <text x="36" y="250" font-family="sans-serif" font-size="15" font-weight="600" fill="#6EE7B7">✓ Above safe threshold across all courses</text>

    <!-- Subject Wise Breakdown -->
    <g transform="translate(0, 310)">
      <!-- Subject 1 -->
      <rect x="0" y="0" width="616" height="120" rx="20" fill="#FFFFFF" stroke="#E2E8F0" stroke-width="2" />
      <text x="24" y="45" font-family="sans-serif" font-size="18" font-weight="800" fill="#1E293B">Mathematics</text>
      <text x="24" y="75" font-family="sans-serif" font-size="14" font-weight="600" fill="#059669">Attended: 24/26 classes</text>
      <text x="24" y="100" font-family="sans-serif" font-size="13" font-weight="500" fill="#64748B">Safe to bunk: 3 more classes</text>
      <text x="540" y="65" font-family="sans-serif" font-size="26" font-weight="900" fill="#059669" text-anchor="middle">92%</text>

      <!-- Subject 2 -->
      <rect x="0" y="145" width="616" height="120" rx="20" fill="#FFFFFF" stroke="#E2E8F0" stroke-width="2" />
      <text x="24" y="190" font-family="sans-serif" font-size="18" font-weight="800" fill="#1E293B">Physics</text>
      <text x="24" y="220" font-family="sans-serif" font-size="14" font-weight="600" fill="#059669">Attended: 20/24 classes</text>
      <text x="24" y="245" font-family="sans-serif" font-size="13" font-weight="500" fill="#64748B">Safe to bunk: 1 more class</text>
      <text x="540" y="210" font-family="sans-serif" font-size="26" font-weight="900" fill="#059669" text-anchor="middle">83%</text>
    </g>
  </g>
`;

// Content 6: Smart Reminders
const screen6 = `
  <rect width="688" height="1255" fill="#F8FAFC" />
  <g transform="translate(36, 20)">
    <text x="0" y="36" font-family="sans-serif" font-size="28" font-weight="900" fill="#0F172A">Smart Reminders</text>
    <text x="0" y="66" font-family="sans-serif" font-size="16" font-weight="600" fill="#64748B">Never Miss a Deadline or Class</text>

    <!-- Notification Simulation Card -->
    <g transform="translate(0, 100)">
      <rect x="0" y="0" width="616" height="125" rx="22" fill="#1E293B" />
      <circle cx="50" cy="50" r="22" fill="#4F46E5" />
      <text x="50" y="58" font-family="sans-serif" font-size="22" fill="#FFFFFF" text-anchor="middle">🔔</text>
      <text x="88" y="44" font-family="sans-serif" font-size="16" font-weight="700" fill="#FFFFFF">Class in 15 Minutes</text>
      <text x="88" y="70" font-family="sans-serif" font-size="14" font-weight="500" fill="#94A3B8">Advanced Algorithms • Room 302</text>
      <text x="88" y="98" font-family="sans-serif" font-size="12" font-weight="600" fill="#818CF8">ClassMate Reminder • Just now</text>

      <rect x="0" y="150" width="616" height="125" rx="22" fill="#1E293B" />
      <circle cx="50" cy="200" r="22" fill="#DC2626" />
      <text x="50" y="208" font-family="sans-serif" font-size="22" fill="#FFFFFF" text-anchor="middle">⚠️</text>
      <text x="88" y="194" font-family="sans-serif" font-size="16" font-weight="700" fill="#FFFFFF">Assignment Due Tonight</text>
      <text x="88" y="220" font-family="sans-serif" font-size="14" font-weight="500" fill="#94A3B8">Operating Systems Lab 3 • 11:59 PM</text>
      <text x="88" y="248" font-family="sans-serif" font-size="12" font-weight="600" fill="#F87171">High Priority • 2 hours left</text>
    </g>
  </g>
`;

// Content 7: Dark Mode
const screen7 = `
  <rect width="688" height="1255" fill="#0B0F19" />
  <g transform="translate(36, 20)">
    <text x="0" y="36" font-family="sans-serif" font-size="28" font-weight="900" fill="#FFFFFF">ClassMate Night Hub</text>
    <text x="0" y="66" font-family="sans-serif" font-size="16" font-weight="600" fill="#818CF8">AMOLED Dark Mode</text>

    <!-- Dark Cards -->
    <g transform="translate(0, 100)">
      <rect x="0" y="0" width="616" height="150" rx="24" fill="#1E1B4B" stroke="#4F46E5" stroke-width="1.5" />
      <text x="36" y="50" font-family="sans-serif" font-size="22" font-weight="800" fill="#FFFFFF">Late Night Study Routine</text>
      <text x="36" y="85" font-family="sans-serif" font-size="16" font-weight="600" fill="#A5B4FC">09:00 PM – 11:30 PM • 3 Sessions</text>
      <text x="36" y="118" font-family="sans-serif" font-size="14" font-weight="500" fill="#94A3B8">Optimized for low battery &amp; zero eye strain</text>

      <rect x="0" y="180" width="616" height="150" rx="24" fill="#111827" stroke="#374151" stroke-width="1.5" />
      <text x="36" y="230" font-family="sans-serif" font-size="22" font-weight="800" fill="#FFFFFF">Midnight Quiz Preparation</text>
      <text x="36" y="265" font-family="sans-serif" font-size="16" font-weight="600" fill="#34D399">Discrete Math Midterm</text>
      <text x="36" y="298" font-family="sans-serif" font-size="14" font-weight="500" fill="#94A3B8">Flashcards review complete</text>
    </g>
  </g>
`;

// Content 8: Complete Organizer
const screen8 = `
  <rect width="688" height="1255" fill="#F8FAFC" />
  <g transform="translate(36, 20)">
    <text x="0" y="36" font-family="sans-serif" font-size="28" font-weight="900" fill="#0F172A">Student Daily Dashboard</text>
    <text x="0" y="66" font-family="sans-serif" font-size="16" font-weight="600" fill="#64748B">Good Morning, Alex!</text>

    <!-- Summary Widgets Grid -->
    <g transform="translate(0, 95)">
      <!-- Widget 1: Classes -->
      <rect x="0" y="0" width="295" height="140" rx="22" fill="#EEF2FF" stroke="#C7D2FE" stroke-width="2" />
      <text x="24" y="42" font-family="sans-serif" font-size="15" font-weight="700" fill="#4F46E5">TODAY'S CLASSES</text>
      <text x="24" y="92" font-family="sans-serif" font-size="38" font-weight="900" fill="#1E1B4B">4</text>
      <text x="24" y="118" font-family="sans-serif" font-size="13" font-weight="600" fill="#6366F1">Next in 25 mins</text>

      <!-- Widget 2: Tasks -->
      <rect x="320" y="0" width="295" height="140" rx="22" fill="#FEF3C7" stroke="#FDE68A" stroke-width="2" />
      <text x="344" y="42" font-family="sans-serif" font-size="15" font-weight="700" fill="#D97706">PENDING TASKS</text>
      <text x="344" y="92" font-family="sans-serif" font-size="38" font-weight="900" fill="#78350F">3</text>
      <text x="344" y="118" font-family="sans-serif" font-size="13" font-weight="600" fill="#B45309">1 Due tonight</text>
    </g>

    <!-- Quick Timetable Preview -->
    <g transform="translate(0, 260)">
      <text x="0" y="24" font-family="sans-serif" font-size="20" font-weight="800" fill="#1E293B">Upcoming Routine</text>
      <rect x="0" y="45" width="616" height="100" rx="20" fill="#FFFFFF" stroke="#E2E8F0" stroke-width="2" />
      <text x="24" y="90" font-family="sans-serif" font-size="18" font-weight="800" fill="#1E293B">Database Systems Lecture</text>
      <text x="24" y="118" font-family="sans-serif" font-size="14" font-weight="600" fill="#4F46E5">10:00 AM • Room 304</text>
    </g>
  </g>
`;

const screenshotsConfig = [
  {
    name: 'screenshot_1_timetable.png',
    title: 'Smart Class Timetable',
    subtitle: 'Color-coded weekly routines at a single glance',
    badgeText: 'Routine Manager',
    badgeColor: '#4F46E5',
    content: screen1,
    isDark: false
  },
  {
    name: 'screenshot_2_assignments.png',
    title: 'Homework &amp; Tasks',
    subtitle: 'Never miss an assignment deadline or exam',
    badgeText: 'Task Tracker',
    badgeColor: '#EF4444',
    content: screen2,
    isDark: false
  },
  {
    name: 'screenshot_3_gpa_calculator.png',
    title: 'Instant GPA Calculator',
    subtitle: 'Track cumulative grades &amp; semester target goals',
    badgeText: 'Grade Tracker',
    badgeColor: '#8B5CF6',
    content: screen3,
    isDark: false
  },
  {
    name: 'screenshot_4_smart_notes.png',
    title: 'Organized Study Notes',
    subtitle: 'Capture lectures, organize tags &amp; share notes',
    badgeText: 'Lecture Notes',
    badgeColor: '#10B981',
    content: screen4,
    isDark: false
  },
  {
    name: 'screenshot_5_attendance.png',
    title: 'Attendance Manager',
    subtitle: 'Smart calculation to stay above 75% criteria',
    badgeText: 'Attendance Safe',
    badgeColor: '#059669',
    content: screen5,
    isDark: false
  },
  {
    name: 'screenshot_6_reminders.png',
    title: 'Timely Reminders',
    subtitle: 'Automated alarm alerts for lectures and quizzes',
    badgeText: 'Smart Alerts',
    badgeColor: '#F59E0B',
    content: screen6,
    isDark: false
  },
  {
    name: 'screenshot_7_dark_mode.png',
    title: 'OLED Dark Mode',
    subtitle: 'Easy on the eyes for late-night study sessions',
    badgeText: 'AMOLED Theme',
    badgeColor: '#818CF8',
    content: screen7,
    isDark: true
  },
  {
    name: 'screenshot_8_student_organizer.png',
    title: 'All-In-One Student Hub',
    subtitle: 'Designed for school, college &amp; university life',
    badgeText: 'Daily Planner',
    badgeColor: '#EC4899',
    content: screen8,
    isDark: false
  }
];

async function generateAll() {
  console.log('Generating Feature Graphic (1024x500)...');
  const featSvg = getFeatureGraphicSvg();
  await sharp(Buffer.from(featSvg)).png().toFile(path.join(outDir, 'feature_graphic_1024x500.png'));

  console.log('Generating App Icon (512x512)...');
  const iconSvg = getAppIconSvg();
  await sharp(Buffer.from(iconSvg)).png().toFile(path.join(outDir, 'app_icon_512x512.png'));

  for (let i = 0; i < screenshotsConfig.length; i++) {
    const sc = screenshotsConfig[i];
    console.log(`Generating ${sc.name} (1080x1920)...`);
    const svg = getScreenshotSvg(i + 1, sc.title, sc.subtitle, sc.badgeText, sc.badgeColor, sc.content, sc.isDark);
    await sharp(Buffer.from(svg)).png().toFile(path.join(outDir, sc.name));
  }

  console.log('ALL ASSETS SUCCESSFULLY GENERATED IN play_store_assets/');
}

generateAll().catch(err => {
  console.error(err);
  process.exit(1);
});
