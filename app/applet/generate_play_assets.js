const fs = require('fs');
const path = require('path');
const sharp = require('/tmp/node_modules/sharp');

const outDir = path.join(__dirname, 'play_store_assets');
if (!fs.existsSync(outDir)) {
  fs.mkdirSync(outDir, { recursive: true });
}

// Reusable Phone Mockup Shell (720 x 1360 inside 1080 x 1920 screenshot)
function getPhoneMockup(contentSvg, isDark = false) {
  const bg = isDark ? '#0B0F10' : '#F8FAFC';
  const border = isDark ? '#1E2D33' : '#E2E8F0';
  const statusColor = isDark ? '#94A3B8' : '#64748B';
  return `
    <g transform="translate(180, 480)">
      <!-- Phone Shadow -->
      <rect x="15" y="25" width="720" height="1360" rx="64" fill="#000000" opacity="0.38" filter="url(#phoneShadow)" />
      
      <!-- Phone Outer Bezel -->
      <rect x="0" y="0" width="720" height="1360" rx="60" fill="#141C24" stroke="#334155" stroke-width="6" />
      
      <!-- Phone Inner Screen -->
      <rect x="16" y="16" width="688" height="1328" rx="48" fill="${bg}" />
      
      <!-- Dynamic Island / Speaker Pill -->
      <rect x="250" y="32" width="220" height="34" rx="17" fill="#0B0F10" />
      <circle cx="430" cy="49" r="6" fill="#1E293B" />
      
      <!-- Status Bar -->
      <text x="56" y="56" font-family="'Inter', -apple-system, sans-serif" font-size="20" font-weight="700" fill="${statusColor}">9:41</text>
      <g transform="translate(605, 41)">
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
      <stop offset="0%" stop-color="#091418" />
      <stop offset="45%" stop-color="#0F2826" />
      <stop offset="100%" stop-color="#0B1C2A" />
    </linearGradient>
    <linearGradient id="textGrad" x1="0%" y1="0%" x2="100%" y2="0%">
      <stop offset="0%" stop-color="#2DD4BF" />
      <stop offset="100%" stop-color="#38BDF8" />
    </linearGradient>
    <linearGradient id="accentGrad" x1="0%" y1="0%" x2="100%" y2="100%">
      <stop offset="0%" stop-color="#0EA5E9" />
      <stop offset="100%" stop-color="#2563EB" />
    </linearGradient>
    <linearGradient id="cardGrad" x1="0%" y1="0%" x2="100%" y2="100%">
      <stop offset="0%" stop-color="#182A2E" stop-opacity="0.9" />
      <stop offset="100%" stop-color="#101D22" stop-opacity="0.95" />
    </linearGradient>
    <filter id="glow" x="-20%" y="-20%" width="140%" height="140%">
      <feGaussianBlur stdDeviation="45" result="blur" />
      <feComposite in="SourceGraphic" in2="blur" operator="over" />
    </filter>
    <filter id="phoneDrop" x="-30%" y="-30%" width="160%" height="160%">
      <feDropShadow dx="-15" dy="25" stdDeviation="30" flood-color="#000000" flood-opacity="0.65" />
    </filter>
  </defs>

  <!-- Background Canvas -->
  <rect width="1024" height="500" fill="url(#bgGrad)" />
  
  <!-- Glowing Background Orbs -->
  <circle cx="820" cy="180" r="230" fill="#0D9488" opacity="0.32" filter="url(#glow)" />
  <circle cx="960" cy="380" r="190" fill="#0284C7" opacity="0.25" filter="url(#glow)" />
  <circle cx="160" cy="460" r="160" fill="#14B8A6" opacity="0.18" filter="url(#glow)" />

  <!-- Left Content Area -->
  <g transform="translate(60, 68)">
    <!-- Category Pill -->
    <rect x="0" y="0" width="250" height="36" rx="18" fill="#134E4A" opacity="0.75" stroke="#2DD4BF" stroke-width="1.5" />
    <circle cx="20" cy="18" r="5" fill="#2DD4BF" />
    <text x="35" y="23" font-family="'Inter', sans-serif" font-size="12" font-weight="800" fill="#E6FFFA" letter-spacing="1">STUDENT PRODUCTIVITY SUITE</text>

    <!-- Main Title with Mortarboard Cap Icon -->
    <g transform="translate(0, 52)">
      <text x="0" y="58" font-family="'Inter', sans-serif" font-size="64" font-weight="900" fill="#FFFFFF" letter-spacing="-1">Class<tspan fill="url(#textGrad)">Mate</tspan></text>
      <text x="0" y="98" font-family="'Inter', sans-serif" font-size="20" font-weight="600" fill="#94A3B8">All-In-One Academic Planner for College &amp; University</text>
    </g>
    
    <!-- Feature Checklist (2 Columns of Real Features) -->
    <g transform="translate(0, 185)">
      <!-- Col 1 -->
      <g transform="translate(0, 0)">
        <circle cx="10" cy="10" r="8" fill="#134E4A" />
        <text x="10" y="14" font-family="sans-serif" font-size="11" font-weight="900" fill="#2DD4BF" text-anchor="middle">✓</text>
        <text x="28" y="15" font-family="'Inter', sans-serif" font-size="15" font-weight="700" fill="#F1F5F9">Class Routine &amp; Timetable</text>
      </g>
      <g transform="translate(0, 36)">
        <circle cx="10" cy="10" r="8" fill="#134E4A" />
        <text x="10" y="14" font-family="sans-serif" font-size="11" font-weight="900" fill="#2DD4BF" text-anchor="middle">✓</text>
        <text x="28" y="15" font-family="'Inter', sans-serif" font-size="15" font-weight="700" fill="#F1F5F9">Assignments &amp; Exams</text>
      </g>
      <g transform="translate(0, 72)">
        <circle cx="10" cy="10" r="8" fill="#134E4A" />
        <text x="10" y="14" font-family="sans-serif" font-size="11" font-weight="900" fill="#2DD4BF" text-anchor="middle">✓</text>
        <text x="28" y="15" font-family="'Inter', sans-serif" font-size="15" font-weight="700" fill="#F1F5F9">Semester CGPA Calculator</text>
      </g>

      <!-- Col 2 -->
      <g transform="translate(250, 0)">
        <circle cx="10" cy="10" r="8" fill="#134E4A" />
        <text x="10" y="14" font-family="sans-serif" font-size="11" font-weight="900" fill="#2DD4BF" text-anchor="middle">✓</text>
        <text x="28" y="15" font-family="'Inter', sans-serif" font-size="15" font-weight="700" fill="#F1F5F9">Cover Page Generator (PDF)</text>
      </g>
      <g transform="translate(250, 36)">
        <circle cx="10" cy="10" r="8" fill="#134E4A" />
        <text x="10" y="14" font-family="sans-serif" font-size="11" font-weight="900" fill="#2DD4BF" text-anchor="middle">✓</text>
        <text x="28" y="15" font-family="'Inter', sans-serif" font-size="15" font-weight="700" fill="#F1F5F9">Lecture Notes &amp; Snaps</text>
      </g>
      <g transform="translate(250, 72)">
        <circle cx="10" cy="10" r="8" fill="#134E4A" />
        <text x="10" y="14" font-family="sans-serif" font-size="11" font-weight="900" fill="#2DD4BF" text-anchor="middle">✓</text>
        <text x="28" y="15" font-family="'Inter', sans-serif" font-size="15" font-weight="700" fill="#F1F5F9">Pomodoro Study Timer</text>
      </g>
    </g>

    <!-- Bottom Offline & Privacy Badge -->
    <g transform="translate(0, 310)">
      <rect x="0" y="0" width="460" height="42" rx="12" fill="#0F172A" stroke="#334155" stroke-width="1.5" />
      <text x="20" y="26" font-family="'Inter', sans-serif" font-size="14" font-weight="800" fill="#38BDF8">⚡ 100% OFFLINE</text>
      <text x="145" y="26" font-family="'Inter', sans-serif" font-size="13" font-weight="500" fill="#94A3B8">• Zero Sign-In • Private &amp; Instant Data</text>
    </g>
  </g>

  <!-- Right Floating Phone Mockup (Angled 3D Isometric Look) -->
  <g transform="translate(680, 40) rotate(5)" filter="url(#phoneDrop)">
    <!-- Outer Frame -->
    <rect x="0" y="0" width="290" height="580" rx="36" fill="#111B20" stroke="#334E58" stroke-width="4" />
    <rect x="8" y="8" width="274" height="564" rx="30" fill="#0B0F10" />

    <!-- Speaker Pill -->
    <rect x="100" y="16" width="90" height="12" rx="6" fill="#000000" />

    <!-- Mini Screen UI -->
    <g transform="translate(18, 38)">
      <!-- App Header -->
      <rect x="0" y="0" width="254" height="32" rx="8" fill="#102528" />
      <text x="12" y="21" font-family="sans-serif" font-size="13" font-weight="800" fill="#2DD4BF">ClassMate</text>
      <text x="240" y="21" font-family="sans-serif" font-size="12" fill="#94A3B8" text-anchor="end">🔔</text>

      <!-- Top Agenda Banner -->
      <rect x="0" y="40" width="254" height="85" rx="12" fill="#0F2F2B" stroke="#1D534B" stroke-width="1" />
      <text x="12" y="60" font-family="sans-serif" font-size="10" font-weight="800" fill="#2DD4BF" letter-spacing="0.5">TODAY'S AGENDA</text>
      <text x="12" y="80" font-family="sans-serif" font-size="13" font-weight="800" fill="#FFFFFF">Database Systems (CSE-311)</text>
      <text x="12" y="100" font-family="sans-serif" font-size="10" font-weight="500" fill="#A7F3D0">10:00 AM • Room 402 • 1 Task Due</text>

      <!-- 2x2 Action Cards Mini -->
      <g transform="translate(0, 135)">
        <rect x="0" y="0" width="122" height="65" rx="10" fill="#142429" stroke="#1E3840" stroke-width="1" />
        <text x="10" y="24" font-family="sans-serif" font-size="14">📖</text>
        <text x="10" y="44" font-family="sans-serif" font-size="10" font-weight="700" fill="#FFFFFF">Class Notes</text>
        <text x="10" y="56" font-family="sans-serif" font-size="8" fill="#38BDF8">14 Topics</text>

        <rect x="132" y="0" width="122" height="65" rx="10" fill="#142429" stroke="#1E3840" stroke-width="1" />
        <text x="142" y="24" font-family="sans-serif" font-size="14">📅</text>
        <text x="142" y="44" font-family="sans-serif" font-size="10" font-weight="700" fill="#FFFFFF">Class Routine</text>
        <text x="142" y="56" font-family="sans-serif" font-size="8" fill="#A78BFA">Weekly Schedule</text>

        <rect x="0" y="73" width="122" height="65" rx="10" fill="#142429" stroke="#1E3840" stroke-width="1" />
        <text x="10" y="97" font-family="sans-serif" font-size="14">📊</text>
        <text x="10" y="117" font-family="sans-serif" font-size="10" font-weight="700" fill="#FFFFFF">CGPA Calc</text>
        <text x="10" y="129" font-family="sans-serif" font-size="8" fill="#34D399">3.88 GPA</text>

        <rect x="132" y="73" width="122" height="65" rx="10" fill="#142429" stroke="#1E3840" stroke-width="1" />
        <text x="142" y="97" font-family="sans-serif" font-size="14">📄</text>
        <text x="142" y="117" font-family="sans-serif" font-size="10" font-weight="700" fill="#FFFFFF">Cover Page</text>
        <text x="142" y="129" font-family="sans-serif" font-size="8" fill="#F472B6">PDF Export</text>
      </g>
    </g>

    <!-- Floating Badge 1: CGPA -->
    <g transform="translate(-40, 260)">
      <rect x="0" y="0" width="115" height="48" rx="24" fill="#042F2C" stroke="#2DD4BF" stroke-width="1.5" />
      <text x="57" y="22" font-family="sans-serif" font-size="11" font-weight="800" fill="#A7F3D0" text-anchor="middle">CGPA</text>
      <text x="57" y="38" font-family="sans-serif" font-size="15" font-weight="900" fill="#2DD4BF" text-anchor="middle">3.88 / 4.00</text>
    </g>

    <!-- Floating Badge 2: PDF Ready -->
    <g transform="translate(180, 410)">
      <rect x="0" y="0" width="125" height="42" rx="21" fill="#1E1B4B" stroke="#818CF8" stroke-width="1.5" />
      <text x="62" y="26" font-family="sans-serif" font-size="12" font-weight="800" fill="#C7D2FE" text-anchor="middle">📄 A4 PDF Export</text>
    </g>
  </g>
</svg>
`;
}

// 2. High-Res App Icon (512 x 512)
function getAppIconSvg() {
  return `
<svg width="512" height="512" viewBox="0 0 512 512" xmlns="http://www.w3.org/2000/svg">
  <defs>
    <radialGradient id="iconBg" cx="50%" cy="30%" r="70%">
      <stop offset="0%" stop-color="#113330" />
      <stop offset="50%" stop-color="#0A1D1C" />
      <stop offset="100%" stop-color="#061211" />
    </radialGradient>
    <linearGradient id="capGrad" x1="0%" y1="0%" x2="100%" y2="100%">
      <stop offset="0%" stop-color="#2DD4BF" />
      <stop offset="50%" stop-color="#0EA5E9" />
      <stop offset="100%" stop-color="#2563EB" />
    </linearGradient>
    <linearGradient id="bookGrad" x1="0%" y1="0%" x2="100%" y2="0%">
      <stop offset="0%" stop-color="#FFFFFF" />
      <stop offset="100%" stop-color="#E2E8F0" />
    </linearGradient>
    <linearGradient id="goldTassel" x1="0%" y1="0%" x2="100%" y2="100%">
      <stop offset="0%" stop-color="#FDE047" />
      <stop offset="100%" stop-color="#F59E0B" />
    </linearGradient>
    <filter id="iconGlow" x="-20%" y="-20%" width="140%" height="140%">
      <feGaussianBlur stdDeviation="30" result="blur" />
      <feComposite in="SourceGraphic" in2="blur" operator="over" />
    </filter>
    <filter id="drop" x="-20%" y="-20%" width="140%" height="140%">
      <feDropShadow dx="0" dy="16" stdDeviation="18" flood-color="#000000" flood-opacity="0.5" />
    </filter>
  </defs>

  <!-- App Icon Squircle Background -->
  <rect width="512" height="512" rx="115" fill="url(#iconBg)" stroke="#164E48" stroke-width="6" />

  <!-- Background Teal Radial Aura -->
  <circle cx="256" cy="230" r="140" fill="#2DD4BF" opacity="0.22" filter="url(#iconGlow)" />

  <!-- Main Academic Icon Group -->
  <g transform="translate(256, 250) scale(3.4)" filter="url(#drop)">
    <!-- Open Book Pages (Bottom) -->
    <path d="M-42,12 C-26,4 -10,8 0,16 C10,8 26,4 42,12 L40,26 C25,18 10,21 0,28 C-10,21 -25,18 -40,26 Z" fill="url(#bookGrad)" />
    <!-- Book Spine Glow -->
    <path d="M-1,16 L-1,28 L1,28 L1,16 Z" fill="#94A3B8" />

    <!-- Mortarboard Cap Diamond Top -->
    <path d="M0,-24 L38,-8 L0,8 L-38,-8 Z" fill="url(#capGrad)" />

    <!-- Cap Headband / Skull Cap -->
    <path d="M-18,-1 C-18,9 0,14 0,14 C0,14 18,9 18,-1 L18,5 C18,13 0,19 0,19 C0,19 -18,13 -18,5 Z" fill="#0284C7" />

    <!-- Tassel Center Button -->
    <circle cx="0" cy="-8" r="3.5" fill="url(#goldTassel)" />

    <!-- Tassel Cord & Fringe -->
    <path d="M0,-8 Q24,-5 27,8 L27,17" fill="none" stroke="url(#goldTassel)" stroke-width="2.6" stroke-linecap="round" />
    <polygon points="24,17 30,17 29,26 25,26" fill="url(#goldTassel)" />
  </g>
</svg>
`;
}

// -------------------------------------------------------------
// REAL SCREENSHOT CONTENTS ACCURATELY MATCHING CLASSMATE
// -------------------------------------------------------------

// Screen 1: Academic Hub & Daily Agenda
const screen1 = `
  <rect width="688" height="1255" fill="#F8FAFC" />
  <g transform="translate(36, 20)">
    <!-- Top Bar -->
    <g transform="translate(0, 0)">
      <rect x="0" y="0" width="46" height="46" rx="14" fill="#CCFBF1" stroke="#99F6E4" stroke-width="1.5" />
      <text x="23" y="30" font-family="sans-serif" font-size="22" text-anchor="middle">🎓</text>
      <text x="60" y="24" font-family="sans-serif" font-size="22" font-weight="900" fill="#0F172A">ClassMate</text>
      <text x="60" y="44" font-family="sans-serif" font-size="13" font-weight="600" fill="#0D9488">Academic Workspace</text>

      <circle cx="585" cy="23" r="20" fill="#F1F5F9" stroke="#E2E8F0" stroke-width="1.5" />
      <text x="585" y="30" font-family="sans-serif" font-size="17" text-anchor="middle">🔔</text>
      <circle cx="598" cy="12" r="5" fill="#2DD4BF" />
    </g>

    <!-- Today's Academic Agenda Banner -->
    <g transform="translate(0, 68)">
      <rect x="0" y="0" width="616" height="155" rx="22" fill="#0F766E" />
      <rect x="24" y="20" width="135" height="26" rx="8" fill="#115E59" />
      <text x="91" y="37" font-family="sans-serif" font-size="11" font-weight="800" fill="#99F6E4" text-anchor="middle">TODAY'S AGENDA</text>
      <text x="175" y="38" font-family="sans-serif" font-size="13" font-weight="600" fill="#CCFBF1">Wednesday</text>

      <text x="24" y="80" font-family="sans-serif" font-size="22" font-weight="900" fill="#FFFFFF">Database Systems (CSE-311)</text>
      <text x="24" y="108" font-family="sans-serif" font-size="15" font-weight="600" fill="#E6FFFA">⏰ 10:00 AM – 11:30 AM • Room 402, Dept Bldg</text>

      <rect x="24" y="122" width="230" height="22" rx="6" fill="#134E4A" />
      <text x="32" y="137" font-family="sans-serif" font-size="11" font-weight="700" fill="#5EEAD4">1 Assignment Due Tonight • 1 Exam Soon</text>
    </g>

    <!-- Academic Workspace 2x3 Grid -->
    <g transform="translate(0, 245)">
      <text x="0" y="20" font-family="sans-serif" font-size="18" font-weight="900" fill="#064E3B">Academic Workspace</text>

      <!-- Row 1 -->
      <g transform="translate(0, 36)">
        <rect x="0" y="0" width="298" height="115" rx="18" fill="#FFFFFF" stroke="#E2E8F0" stroke-width="2" />
        <rect x="18" y="18" width="44" height="44" rx="12" fill="#EFF6FF" />
        <text x="40" y="47" font-family="sans-serif" font-size="22" text-anchor="middle">📖</text>
        <text x="18" y="85" font-family="sans-serif" font-size="16" font-weight="800" fill="#1E293B">Class Notes &amp;</text>
        <text x="18" y="103" font-family="sans-serif" font-size="16" font-weight="800" fill="#1E293B">Photos</text>

        <rect x="318" y="0" width="298" height="115" rx="18" fill="#FFFFFF" stroke="#E2E8F0" stroke-width="2" />
        <rect x="336" y="18" width="44" height="44" rx="12" fill="#F3E8FF" />
        <text x="358" y="47" font-family="sans-serif" font-size="22" text-anchor="middle">📅</text>
        <text x="336" y="85" font-family="sans-serif" font-size="16" font-weight="800" fill="#1E293B">Study</text>
        <text x="336" y="103" font-family="sans-serif" font-size="16" font-weight="800" fill="#1E293B">Planner</text>
      </g>

      <!-- Row 2 -->
      <g transform="translate(0, 166)">
        <rect x="0" y="0" width="298" height="115" rx="18" fill="#FFFFFF" stroke="#E2E8F0" stroke-width="2" />
        <rect x="18" y="18" width="44" height="44" rx="12" fill="#E0F7FA" />
        <text x="40" y="47" font-family="sans-serif" font-size="22" text-anchor="middle">🧮</text>
        <text x="18" y="85" font-family="sans-serif" font-size="16" font-weight="800" fill="#1E293B">CGPA</text>
        <text x="18" y="103" font-family="sans-serif" font-size="16" font-weight="800" fill="#1E293B">Calculator</text>

        <rect x="318" y="0" width="298" height="115" rx="18" fill="#FFFFFF" stroke="#E2E8F0" stroke-width="2" />
        <rect x="336" y="18" width="44" height="44" rx="12" fill="#DCFCE7" />
        <text x="358" y="47" font-family="sans-serif" font-size="22" text-anchor="middle">💳</text>
        <text x="336" y="85" font-family="sans-serif" font-size="16" font-weight="800" fill="#1E293B">Tuition Fee</text>
        <text x="336" y="103" font-family="sans-serif" font-size="16" font-weight="800" fill="#1E293B">Calculator</text>
      </g>

      <!-- Row 3 -->
      <g transform="translate(0, 296)">
        <rect x="0" y="0" width="298" height="115" rx="18" fill="#FFFFFF" stroke="#E2E8F0" stroke-width="2" />
        <rect x="18" y="18" width="44" height="44" rx="12" fill="#FEF3C7" />
        <text x="40" y="47" font-family="sans-serif" font-size="22" text-anchor="middle">📝</text>
        <rect x="210" y="18" width="70" height="26" rx="8" fill="#FEE2E2" />
        <text x="245" y="35" font-family="sans-serif" font-size="12" font-weight="800" fill="#DC2626" text-anchor="middle">2 Due</text>
        <text x="18" y="85" font-family="sans-serif" font-size="16" font-weight="800" fill="#1E293B">Assignments</text>
        <text x="18" y="103" font-family="sans-serif" font-size="13" font-weight="600" fill="#64748B">Due date alerts</text>

        <rect x="318" y="0" width="298" height="115" rx="18" fill="#FFFFFF" stroke="#E2E8F0" stroke-width="2" />
        <rect x="336" y="18" width="44" height="44" rx="12" fill="#FFE4E6" />
        <text x="358" y="47" font-family="sans-serif" font-size="22" text-anchor="middle">🎯</text>
        <rect x="528" y="18" width="70" height="26" rx="8" fill="#FEF3C7" />
        <text x="563" y="35" font-family="sans-serif" font-size="12" font-weight="800" fill="#D97706" text-anchor="middle">1 Soon</text>
        <text x="336" y="85" font-family="sans-serif" font-size="16" font-weight="800" fill="#1E293B">Exams</text>
        <text x="336" y="103" font-family="sans-serif" font-size="13" font-weight="600" fill="#64748B">Countdown timer</text>
      </g>

      <!-- Row 4 -->
      <g transform="translate(0, 426)">
        <rect x="0" y="0" width="298" height="115" rx="18" fill="#FFFFFF" stroke="#E2E8F0" stroke-width="2" />
        <rect x="18" y="18" width="44" height="44" rx="12" fill="#EFF6FF" />
        <text x="40" y="47" font-family="sans-serif" font-size="22" text-anchor="middle">🗓️</text>
        <text x="18" y="85" font-family="sans-serif" font-size="16" font-weight="800" fill="#1E293B">Class Routine</text>
        <text x="18" y="103" font-family="sans-serif" font-size="13" font-weight="600" fill="#64748B">Weekly timetable</text>

        <rect x="318" y="0" width="298" height="115" rx="18" fill="#FFFFFF" stroke="#E2E8F0" stroke-width="2" />
        <rect x="336" y="18" width="44" height="44" rx="12" fill="#FFE4E6" />
        <text x="358" y="47" font-family="sans-serif" font-size="22" text-anchor="middle">⏱️</text>
        <text x="336" y="85" font-family="sans-serif" font-size="16" font-weight="800" fill="#1E293B">Focus Timer</text>
        <text x="336" y="103" font-family="sans-serif" font-size="13" font-weight="600" fill="#64748B">Pomodoro study</text>
      </g>

      <!-- Row 5 (Standout Features) -->
      <g transform="translate(0, 556)">
        <rect x="0" y="0" width="298" height="115" rx="18" fill="#FFFFFF" stroke="#E2E8F0" stroke-width="2" />
        <rect x="18" y="18" width="44" height="44" rx="12" fill="#EEF2FF" />
        <text x="40" y="47" font-family="sans-serif" font-size="22" text-anchor="middle">📄</text>
        <text x="18" y="85" font-family="sans-serif" font-size="16" font-weight="800" fill="#1E293B">Cover Page</text>
        <text x="18" y="103" font-family="sans-serif" font-size="16" font-weight="800" fill="#4F46E5">Generator</text>

        <rect x="318" y="0" width="298" height="115" rx="18" fill="#FFFFFF" stroke="#E2E8F0" stroke-width="2" />
        <rect x="336" y="18" width="44" height="44" rx="12" fill="#FEF9C3" />
        <text x="358" y="47" font-family="sans-serif" font-size="22" text-anchor="middle">👨‍🏫</text>
        <text x="336" y="85" font-family="sans-serif" font-size="16" font-weight="800" fill="#1E293B">Faculty Info</text>
        <text x="336" y="103" font-family="sans-serif" font-size="13" font-weight="600" fill="#64748B">Teacher directory</text>
      </g>
    </g>
  </g>
`;

// Screen 2: Class Routine & Weekly Timetable
const screen2 = `
  <rect width="688" height="1255" fill="#F8FAFC" />
  <g transform="translate(36, 20)">
    <!-- Top Bar -->
    <text x="0" y="32" font-family="sans-serif" font-size="26" font-weight="900" fill="#0F172A">Class Routine</text>
    <text x="0" y="58" font-family="sans-serif" font-size="14" font-weight="600" fill="#64748B">Weekly Schedule &amp; Lecture Timetable</text>

    <!-- Day Selector Tabs -->
    <g transform="translate(0, 80)">
      <rect x="0" y="0" width="88" height="42" rx="12" fill="#FFFFFF" stroke="#CBD5E1" stroke-width="1.5" />
      <text x="44" y="26" font-family="sans-serif" font-size="14" font-weight="700" fill="#475569" text-anchor="middle">Mon</text>

      <rect x="100" y="0" width="88" height="42" rx="12" fill="#FFFFFF" stroke="#CBD5E1" stroke-width="1.5" />
      <text x="144" y="26" font-family="sans-serif" font-size="14" font-weight="700" fill="#475569" text-anchor="middle">Tue</text>

      <rect x="200" y="0" width="105" height="42" rx="12" fill="#0F766E" />
      <text x="252" y="26" font-family="sans-serif" font-size="14" font-weight="800" fill="#FFFFFF" text-anchor="middle">Wed (Today)</text>

      <rect x="318" y="0" width="88" height="42" rx="12" fill="#FFFFFF" stroke="#CBD5E1" stroke-width="1.5" />
      <text x="362" y="26" font-family="sans-serif" font-size="14" font-weight="700" fill="#475569" text-anchor="middle">Thu</text>

      <rect x="418" y="0" width="88" height="42" rx="12" fill="#FFFFFF" stroke="#CBD5E1" stroke-width="1.5" />
      <text x="462" y="26" font-family="sans-serif" font-size="14" font-weight="700" fill="#475569" text-anchor="middle">Fri</text>

      <rect x="518" y="0" width="88" height="42" rx="12" fill="#FFFFFF" stroke="#CBD5E1" stroke-width="1.5" />
      <text x="562" y="26" font-family="sans-serif" font-size="14" font-weight="700" fill="#475569" text-anchor="middle">Sat</text>
    </g>

    <!-- Routine Cards List -->
    <g transform="translate(0, 150)">
      <!-- Item 1: Active Now -->
      <rect x="0" y="0" width="616" height="160" rx="22" fill="#F0FDF4" stroke="#86EFAC" stroke-width="2" />
      <rect x="24" y="22" width="105" height="28" rx="8" fill="#DCFCE7" />
      <text x="76" y="41" font-family="sans-serif" font-size="12" font-weight="800" fill="#15803D" text-anchor="middle">● IN PROGRESS</text>
      
      <text x="24" y="85" font-family="sans-serif" font-size="22" font-weight="900" fill="#1E293B">Database Management Systems</text>
      <text x="24" y="112" font-family="sans-serif" font-size="15" font-weight="700" fill="#0F766E">CSE-311 • Section A</text>
      <text x="24" y="138" font-family="sans-serif" font-size="14" font-weight="600" fill="#64748B">⏰ 09:30 AM – 11:00 AM • 📍 Room 402 • 👨‍🏫 Prof. Dr. Sarah Jenkins</text>

      <!-- Item 2: Upcoming Next -->
      <rect x="0" y="180" width="616" height="150" rx="22" fill="#FFFFFF" stroke="#E2E8F0" stroke-width="2" />
      <rect x="24" y="202" width="90" height="28" rx="8" fill="#EFF6FF" />
      <text x="69" y="221" font-family="sans-serif" font-size="12" font-weight="800" fill="#2563EB" text-anchor="middle">NEXT CLASS</text>

      <text x="24" y="265" font-family="sans-serif" font-size="22" font-weight="900" fill="#1E293B">Linear Algebra &amp; Matrices</text>
      <text x="24" y="292" font-family="sans-serif" font-size="15" font-weight="700" fill="#3B82F6">MAT-205 • Section B</text>
      <text x="24" y="318" font-family="sans-serif" font-size="14" font-weight="600" fill="#64748B">⏰ 11:30 AM – 01:00 PM • 📍 Room 205 • 👨‍🏫 Dr. Robert Vance</text>

      <!-- Item 3: Lab Session -->
      <rect x="0" y="350" width="616" height="150" rx="22" fill="#FFFFFF" stroke="#E2E8F0" stroke-width="2" />
      <rect x="24" y="372" width="110" height="28" rx="8" fill="#FEF3C7" />
      <text x="79" y="391" font-family="sans-serif" font-size="12" font-weight="800" fill="#B45309" text-anchor="middle">LAB SESSION</text>

      <text x="24" y="435" font-family="sans-serif" font-size="22" font-weight="900" fill="#1E293B">Microprocessor &amp; Interfacing Lab</text>
      <text x="24" y="462" font-family="sans-serif" font-size="15" font-weight="700" fill="#D97706">CSE-316 • Hardware Lab</text>
      <text x="24" y="488" font-family="sans-serif" font-size="14" font-weight="600" fill="#64748B">⏰ 02:00 PM – 04:30 PM • 📍 Micro Lab 2 • 👨‍🏫 Engr. David Miller</text>
    </g>
  </g>
`;

// Screen 3: Assignments & Exam Countdown
const screen3 = `
  <rect width="688" height="1255" fill="#F8FAFC" />
  <g transform="translate(36, 20)">
    <text x="0" y="32" font-family="sans-serif" font-size="26" font-weight="900" fill="#0F172A">Assignments &amp; Exams</text>
    <text x="0" y="58" font-family="sans-serif" font-size="14" font-weight="600" fill="#64748B">Deadlines, Exam Schedules &amp; Urgent Countdowns</text>

    <!-- Big Exam Countdown Alert Card -->
    <g transform="translate(0, 80)">
      <rect x="0" y="0" width="616" height="165" rx="24" fill="#991B1B" />
      <rect x="24" y="20" width="140" height="28" rx="8" fill="#7F1D1D" />
      <text x="94" y="39" font-family="sans-serif" font-size="12" font-weight="800" fill="#FECACA" text-anchor="middle">MIDTERM EXAM</text>
      
      <text x="24" y="82" font-family="sans-serif" font-size="24" font-weight="900" fill="#FFFFFF">Data Structures &amp; Algorithms</text>
      <text x="24" y="110" font-family="sans-serif" font-size="15" font-weight="600" fill="#FCA5A5">📅 Oct 24, 2026 • 10:00 AM • Room 304 (Main Hall)</text>

      <g transform="translate(24, 125)">
        <rect x="0" y="0" width="190" height="26" rx="8" fill="#B91C1C" />
        <text x="95" y="17" font-family="sans-serif" font-size="12" font-weight="800" fill="#FFFFFF" text-anchor="middle">⏳ COUNTDOWN: 3 DAYS LEFT</text>
      </g>
    </g>

    <!-- Upcoming Assignments Section -->
    <g transform="translate(0, 275)">
      <text x="0" y="24" font-family="sans-serif" font-size="20" font-weight="900" fill="#1E293B">Active Assignments</text>

      <!-- Assignment 1 -->
      <g transform="translate(0, 42)">
        <rect x="0" y="0" width="616" height="150" rx="20" fill="#FFFFFF" stroke="#E2E8F0" stroke-width="2" />
        <rect x="24" y="20" width="105" height="26" rx="6" fill="#FEE2E2" />
        <text x="76" y="37" font-family="sans-serif" font-size="11" font-weight="800" fill="#DC2626" text-anchor="middle">HIGH PRIORITY</text>

        <rect x="140" y="20" width="95" height="26" rx="6" fill="#EFF6FF" />
        <text x="187" y="37" font-family="sans-serif" font-size="11" font-weight="700" fill="#2563EB" text-anchor="middle">CSE-320</text>

        <text x="24" y="80" font-family="sans-serif" font-size="20" font-weight="900" fill="#1E293B">Operating Systems Lab Report #3</text>
        <text x="24" y="108" font-family="sans-serif" font-size="14" font-weight="600" fill="#DC2626">⏰ Due Tonight at 11:59 PM • 4 Hours Left</text>

        <!-- Progress bar -->
        <rect x="24" y="126" width="450" height="8" rx="4" fill="#E2E8F0" />
        <rect x="24" y="126" width="360" height="8" rx="4" fill="#DC2626" />
        <text x="490" y="134" font-family="sans-serif" font-size="12" font-weight="700" fill="#64748B">80% Done</text>
      </g>

      <!-- Assignment 2 -->
      <g transform="translate(0, 210)">
        <rect x="0" y="0" width="616" height="150" rx="20" fill="#FFFFFF" stroke="#E2E8F0" stroke-width="2" />
        <rect x="24" y="20" width="115" height="26" rx="6" fill="#FEF3C7" />
        <text x="81" y="37" font-family="sans-serif" font-size="11" font-weight="800" fill="#D97706" text-anchor="middle">MEDIUM PRIORITY</text>

        <rect x="150" y="20" width="95" height="26" rx="6" fill="#F3E8FF" />
        <text x="197" y="37" font-family="sans-serif" font-size="11" font-weight="700" fill="#7E22CE" text-anchor="middle">SWE-204</text>

        <text x="24" y="80" font-family="sans-serif" font-size="20" font-weight="900" fill="#1E293B">Sprint 2 Architecture Diagram &amp; Case Study</text>
        <text x="24" y="108" font-family="sans-serif" font-size="14" font-weight="600" fill="#64748B">⏰ Due Friday, Oct 23 • 3 Days Left</text>

        <!-- Progress bar -->
        <rect x="24" y="126" width="450" height="8" rx="4" fill="#E2E8F0" />
        <rect x="24" y="126" width="180" height="8" rx="4" fill="#3B82F6" />
        <text x="490" y="134" font-family="sans-serif" font-size="12" font-weight="700" fill="#64748B">40% Done</text>
      </g>
    </g>
  </g>
`;

// Screen 4: Semester CGPA & GPA Calculator
const screen4 = `
  <rect width="688" height="1255" fill="#F8FAFC" />
  <g transform="translate(36, 20)">
    <text x="0" y="32" font-family="sans-serif" font-size="26" font-weight="900" fill="#0F172A">CGPA &amp; GPA Calculator</text>
    <text x="0" y="58" font-family="sans-serif" font-size="14" font-weight="600" fill="#64748B">Semester-wise GPA, Credits &amp; Future Target Goal</text>

    <!-- Top CGPA Showcase Card -->
    <g transform="translate(0, 80)">
      <rect x="0" y="0" width="616" height="210" rx="26" fill="#0F766E" />
      <text x="36" y="44" font-family="sans-serif" font-size="14" font-weight="800" fill="#99F6E4" letter-spacing="1">CUMULATIVE GRADE POINT AVERAGE</text>

      <text x="36" y="120" font-family="sans-serif" font-size="68" font-weight="900" fill="#FFFFFF">3.88</text>
      <text x="205" y="112" font-family="sans-serif" font-size="24" font-weight="700" fill="#99F6E4">/ 4.00</text>

      <!-- Grade Badge -->
      <rect x="440" y="45" width="140" height="120" rx="20" fill="#115E59" />
      <text x="510" y="115" font-family="sans-serif" font-size="48" font-weight="900" fill="#2DD4BF" text-anchor="middle">A+</text>
      <text x="510" y="145" font-family="sans-serif" font-size="13" font-weight="800" fill="#CCFBF1" text-anchor="middle">EXCELLENT</text>

      <!-- Stats Line -->
      <rect x="36" y="155" width="370" height="34" rx="10" fill="#134E4A" />
      <text x="50" y="177" font-family="sans-serif" font-size="13" font-weight="700" fill="#CCFBF1">Total Completed: 48.0 Credits • 4 Semesters</text>
    </g>

    <!-- Semester Course Breakdown -->
    <g transform="translate(0, 315)">
      <text x="0" y="24" font-family="sans-serif" font-size="20" font-weight="900" fill="#1E293B">Current Semester Breakdown (Fall 2026)</text>
      
      <!-- Course 1 -->
      <g transform="translate(0, 42)">
        <rect x="0" y="0" width="616" height="85" rx="18" fill="#FFFFFF" stroke="#E2E8F0" stroke-width="2" />
        <text x="24" y="38" font-family="sans-serif" font-size="17" font-weight="800" fill="#1E293B">Software Engineering (SWE-301)</text>
        <text x="24" y="62" font-family="sans-serif" font-size="13" font-weight="600" fill="#64748B">3.0 Credit Hours • Grade Point: 4.00</text>
        <rect x="520" y="22" width="70" height="42" rx="12" fill="#DCFCE7" />
        <text x="555" y="49" font-family="sans-serif" font-size="17" font-weight="900" fill="#15803D" text-anchor="middle">A+</text>
      </g>

      <!-- Course 2 -->
      <g transform="translate(0, 142)">
        <rect x="0" y="0" width="616" height="85" rx="18" fill="#FFFFFF" stroke="#E2E8F0" stroke-width="2" />
        <text x="24" y="38" font-family="sans-serif" font-size="17" font-weight="800" fill="#1E293B">Computer Networks (CSE-325)</text>
        <text x="24" y="62" font-family="sans-serif" font-size="13" font-weight="600" fill="#64748B">3.0 Credit Hours • Grade Point: 3.75</text>
        <rect x="520" y="22" width="70" height="42" rx="12" fill="#EFF6FF" />
        <text x="555" y="49" font-family="sans-serif" font-size="17" font-weight="900" fill="#2563EB" text-anchor="middle">A</text>
      </g>

      <!-- Course 3 -->
      <g transform="translate(0, 242)">
        <rect x="0" y="0" width="616" height="85" rx="18" fill="#FFFFFF" stroke="#E2E8F0" stroke-width="2" />
        <text x="24" y="38" font-family="sans-serif" font-size="17" font-weight="800" fill="#1E293B">Artificial Intelligence (CSE-411)</text>
        <text x="24" y="62" font-family="sans-serif" font-size="13" font-weight="600" fill="#64748B">3.0 Credit Hours • Grade Point: 4.00</text>
        <rect x="520" y="22" width="70" height="42" rx="12" fill="#DCFCE7" />
        <text x="555" y="49" font-family="sans-serif" font-size="17" font-weight="900" fill="#15803D" text-anchor="middle">A+</text>
      </g>

      <!-- Course 4 -->
      <g transform="translate(0, 342)">
        <rect x="0" y="0" width="616" height="85" rx="18" fill="#FFFFFF" stroke="#E2E8F0" stroke-width="2" />
        <text x="24" y="38" font-family="sans-serif" font-size="17" font-weight="800" fill="#1E293B">Database Lab (CSE-312)</text>
        <text x="24" y="62" font-family="sans-serif" font-size="13" font-weight="600" fill="#64748B">1.5 Credit Hours • Grade Point: 4.00</text>
        <rect x="520" y="22" width="70" height="42" rx="12" fill="#DCFCE7" />
        <text x="555" y="49" font-family="sans-serif" font-size="17" font-weight="900" fill="#15803D" text-anchor="middle">A+</text>
      </g>
    </g>
  </g>
`;

// Screen 5: Assignment Cover Page Generator (Unique Standout!)
const screen5 = `
  <rect width="688" height="1255" fill="#F8FAFC" />
  <g transform="translate(36, 20)">
    <text x="0" y="32" font-family="sans-serif" font-size="26" font-weight="900" fill="#0F172A">Cover Page Generator</text>
    <text x="0" y="58" font-family="sans-serif" font-size="14" font-weight="600" fill="#64748B">Instant University Assignment Cover Pages (PDF / PNG)</text>

    <!-- Template Switcher Pills -->
    <g transform="translate(0, 78)">
      <rect x="0" y="0" width="145" height="38" rx="10" fill="#4338CA" />
      <text x="72" y="24" font-family="sans-serif" font-size="13" font-weight="800" fill="#FFFFFF" text-anchor="middle">✓ Modern Academic</text>

      <rect x="155" y="0" width="125" height="38" rx="10" fill="#FFFFFF" stroke="#CBD5E1" stroke-width="1.5" />
      <text x="217" y="24" font-family="sans-serif" font-size="13" font-weight="700" fill="#475569" text-anchor="middle">Classic Border</text>

      <rect x="290" y="0" width="125" height="38" rx="10" fill="#FFFFFF" stroke="#CBD5E1" stroke-width="1.5" />
      <text x="352" y="24" font-family="sans-serif" font-size="13" font-weight="700" fill="#475569" text-anchor="middle">Clean Minimal</text>
    </g>

    <!-- Live A4 Sheet Preview Mockup -->
    <g transform="translate(30, 135)">
      <!-- A4 Sheet Paper with Border -->
      <rect x="0" y="0" width="556" height="660" rx="8" fill="#FFFFFF" stroke="#CBD5E1" stroke-width="2" />
      <rect x="16" y="16" width="524" height="628" fill="none" stroke="#4338CA" stroke-width="1.5" />

      <!-- University Name & Logo Header -->
      <text x="278" y="70" font-family="serif" font-size="20" font-weight="900" fill="#1E1B4B" text-anchor="middle">METROPOLITAN UNIVERSITY</text>
      <text x="278" y="95" font-family="sans-serif" font-size="13" font-weight="700" fill="#4338CA" text-anchor="middle">Department of Computer Science &amp; Engineering</text>

      <line x1="160" y1="115" x2="396" y2="115" stroke="#E2E8F0" stroke-width="1.5" />

      <!-- Assignment Title -->
      <text x="278" y="160" font-family="sans-serif" font-size="13" font-weight="800" fill="#64748B" text-anchor="middle">AN ASSIGNMENT ON</text>
      <text x="278" y="200" font-family="serif" font-size="22" font-weight="900" fill="#0F172A" text-anchor="middle">Distributed Cloud Systems &amp; Microservices</text>
      <text x="278" y="230" font-family="sans-serif" font-size="14" font-weight="700" fill="#4338CA" text-anchor="middle">Course: CSE-401 • Advanced Cloud Computing</text>

      <!-- Two Information Boxes (Submitted To & Submitted By) -->
      <g transform="translate(36, 275)">
        <!-- Submitted To Box -->
        <rect x="0" y="0" width="230" height="175" rx="10" fill="#F8FAFC" stroke="#E2E8F0" stroke-width="1.5" />
        <text x="18" y="32" font-family="sans-serif" font-size="13" font-weight="800" fill="#4338CA">SUBMITTED TO:</text>
        <text x="18" y="65" font-family="sans-serif" font-size="15" font-weight="900" fill="#1E293B">Dr. Alan Turing</text>
        <text x="18" y="90" font-family="sans-serif" font-size="12" font-weight="600" fill="#64748B">Professor &amp; Chairman</text>
        <text x="18" y="112" font-family="sans-serif" font-size="12" font-weight="600" fill="#64748B">Dept. of Computer Science</text>
        <text x="18" y="145" font-family="sans-serif" font-size="11" font-weight="700" fill="#0D9488">Office: Room 408</text>

        <!-- Submitted By Box -->
        <rect x="254" y="0" width="230" height="175" rx="10" fill="#F8FAFC" stroke="#E2E8F0" stroke-width="1.5" />
        <text x="272" y="32" font-family="sans-serif" font-size="13" font-weight="800" fill="#4338CA">SUBMITTED BY:</text>
        <text x="272" y="65" font-family="sans-serif" font-size="15" font-weight="900" fill="#1E293B">Alex Rahman</text>
        <text x="272" y="90" font-family="sans-serif" font-size="12" font-weight="600" fill="#64748B">Student ID: 2024-10-842</text>
        <text x="272" y="112" font-family="sans-serif" font-size="12" font-weight="600" fill="#64748B">Intake 48 • Section A</text>
        <text x="272" y="145" font-family="sans-serif" font-size="11" font-weight="700" fill="#0D9488">Semester: 6th (Fall 2026)</text>
      </g>

      <!-- Date of Submission -->
      <text x="278" y="500" font-family="sans-serif" font-size="13" font-weight="700" fill="#1E293B" text-anchor="middle">Date of Submission: October 20, 2026</text>
    </g>

    <!-- Download Actions -->
    <g transform="translate(30, 815)">
      <rect x="0" y="0" width="268" height="52" rx="14" fill="#4338CA" />
      <text x="134" y="32" font-family="sans-serif" font-size="15" font-weight="800" fill="#FFFFFF" text-anchor="middle">📥 Export A4 PDF</text>

      <rect x="288" y="0" width="268" height="52" rx="14" fill="#FFFFFF" stroke="#4338CA" stroke-width="2" />
      <text x="422" y="32" font-family="sans-serif" font-size="15" font-weight="800" fill="#4338CA" text-anchor="middle">🖼️ Save as Image</text>
    </g>
  </g>
`;

// Screen 6: Lecture Notes & Board Photos
const screen6 = `
  <rect width="688" height="1255" fill="#F8FAFC" />
  <g transform="translate(36, 20)">
    <text x="0" y="32" font-family="sans-serif" font-size="26" font-weight="900" fill="#0F172A">Class Notes &amp; Photos</text>
    <text x="0" y="58" font-family="sans-serif" font-size="14" font-weight="600" fill="#64748B">Organized by Courses, Topics &amp; Blackboard Camera Snaps</text>

    <!-- Search Bar -->
    <g transform="translate(0, 80)">
      <rect x="0" y="0" width="616" height="54" rx="16" fill="#FFFFFF" stroke="#CBD5E1" stroke-width="1.5" />
      <text x="24" y="34" font-family="sans-serif" font-size="15" font-weight="500" fill="#94A3B8">🔍 Search lecture notes, topics, formulas...</text>
    </g>

    <!-- Course Notes Card 1 -->
    <g transform="translate(0, 155)">
      <rect x="0" y="0" width="616" height="195" rx="22" fill="#FFFFFF" stroke="#E2E8F0" stroke-width="2" />
      <rect x="24" y="24" width="95" height="28" rx="8" fill="#EFF6FF" />
      <text x="71" y="43" font-family="sans-serif" font-size="12" font-weight="800" fill="#2563EB" text-anchor="middle">CSE-202</text>
      
      <text x="24" y="85" font-family="sans-serif" font-size="20" font-weight="900" fill="#1E293B">AVL Tree Rotations &amp; Balancing</text>
      <text x="24" y="112" font-family="sans-serif" font-size="14" font-weight="500" fill="#64748B">Single LL/RR rotation vs Double LR/RL rotations with step-by-step tree rebalancing proof...</text>

      <!-- Attached Whiteboard Snap Thumbnail Pill -->
      <g transform="translate(24, 135)">
        <rect x="0" y="0" width="220" height="34" rx="8" fill="#F1F5F9" stroke="#CBD5E1" stroke-width="1" />
        <text x="14" y="22" font-family="sans-serif" font-size="13">📷</text>
        <text x="36" y="22" font-family="sans-serif" font-size="12" font-weight="700" fill="#334155">Board_Photo_Lecture08.jpg</text>
      </g>

      <rect x="490" y="135" width="102" height="34" rx="8" fill="#EFF6FF" />
      <text x="541" y="22" font-family="sans-serif" font-size="12" font-weight="800" fill="#2563EB" text-anchor="middle">📤 Share Note</text>
    </g>

    <!-- Course Notes Card 2 -->
    <g transform="translate(0, 370)">
      <rect x="0" y="0" width="616" height="195" rx="22" fill="#FFFFFF" stroke="#E2E8F0" stroke-width="2" />
      <rect x="24" y="24" width="95" height="28" rx="8" fill="#F3E8FF" />
      <text x="71" y="43" font-family="sans-serif" font-size="12" font-weight="800" fill="#7E22CE" text-anchor="middle">MAT-201</text>
      
      <text x="24" y="85" font-family="sans-serif" font-size="20" font-weight="900" fill="#1E293B">Eigenvalues, Eigenvectors &amp; Diagonalization</text>
      <text x="24" y="112" font-family="sans-serif" font-size="14" font-weight="500" fill="#64748B">Characteristic equation det(A - λI) = 0 solved examples for 3x3 symmetric matrix diagonalization...</text>

      <!-- Attached Slide Thumbnail Pill -->
      <g transform="translate(24, 135)">
        <rect x="0" y="0" width="200" height="34" rx="8" fill="#F1F5F9" stroke="#CBD5E1" stroke-width="1" />
        <text x="14" y="22" font-family="sans-serif" font-size="13">📸</text>
        <text x="36" y="22" font-family="sans-serif" font-size="12" font-weight="700" fill="#334155">LectureSlide_04.png</text>
      </g>

      <rect x="490" y="135" width="102" height="34" rx="8" fill="#F3E8FF" />
      <text x="541" y="22" font-family="sans-serif" font-size="12" font-weight="800" fill="#7E22CE" text-anchor="middle">📤 Share Note</text>
    </g>
  </g>
`;

// Screen 7: Pomodoro Focus Study Timer
const screen7 = `
  <rect width="688" height="1255" fill="#0B0F10" />
  <g transform="translate(36, 20)">
    <text x="0" y="32" font-family="sans-serif" font-size="26" font-weight="900" fill="#FFFFFF">Deep Work Focus Timer</text>
    <text x="0" y="58" font-family="sans-serif" font-size="14" font-weight="600" fill="#2DD4BF">Pomodoro Study Intervals &amp; Productivity Streak</text>

    <!-- Circular Timer Centerpiece -->
    <g transform="translate(308, 260)">
      <!-- Outer Track Glow -->
      <circle cx="0" cy="0" r="160" fill="none" stroke="#132B2B" stroke-width="18" />
      <!-- Animated Active Ring -->
      <circle cx="0" cy="0" r="160" fill="none" stroke="#2DD4BF" stroke-width="18" stroke-dasharray="1005" stroke-dashoffset="240" stroke-linecap="round" />

      <!-- Center Labels -->
      <text x="0" y="-45" font-family="sans-serif" font-size="16" font-weight="800" fill="#F59E0B" text-anchor="middle">🔥 FOCUS SESSION</text>
      <text x="0" y="25" font-family="sans-serif" font-size="68" font-weight="900" fill="#FFFFFF" text-anchor="middle">24:18</text>
      <text x="0" y="70" font-family="sans-serif" font-size="16" font-weight="700" fill="#94A3B8" text-anchor="middle">Interval 3 of 4 (25m / 5m)</text>
    </g>

    <!-- Timer Control Buttons -->
    <g transform="translate(144, 480)">
      <rect x="0" y="0" width="150" height="56" rx="28" fill="#134E4A" stroke="#2DD4BF" stroke-width="2" />
      <text x="75" y="35" font-family="sans-serif" font-size="16" font-weight="800" fill="#2DD4BF" text-anchor="middle">⏸️ PAUSE</text>

      <rect x="175" y="0" width="150" height="56" rx="28" fill="#1E293B" stroke="#475569" stroke-width="1.5" />
      <text x="250" y="35" font-family="sans-serif" font-size="16" font-weight="800" fill="#FFFFFF" text-anchor="middle">⏭️ SKIP</text>
    </g>

    <!-- Productivity Insights Summary Card -->
    <g transform="translate(0, 580)">
      <rect x="0" y="0" width="616" height="190" rx="24" fill="#10191D" stroke="#1F363F" stroke-width="1.5" />
      <text x="28" y="38" font-family="sans-serif" font-size="14" font-weight="800" fill="#2DD4BF" letter-spacing="0.5">TODAY'S PRODUCTIVITY STATS</text>

      <!-- 3 Metrics Columns -->
      <g transform="translate(28, 65)">
        <rect x="0" y="0" width="170" height="95" rx="14" fill="#15262B" />
        <text x="20" y="32" font-family="sans-serif" font-size="12" font-weight="700" fill="#94A3B8">TOTAL FOCUS</text>
        <text x="20" y="68" font-family="sans-serif" font-size="24" font-weight="900" fill="#FFFFFF">2h 45m</text>
        <text x="20" y="85" font-family="sans-serif" font-size="11" font-weight="600" fill="#34D399">+35m vs yesterday</text>

        <rect x="195" y="0" width="170" height="95" rx="14" fill="#15262B" />
        <text x="215" y="32" font-family="sans-serif" font-size="12" font-weight="700" fill="#94A3B8">SESSIONS</text>
        <text x="215" y="68" font-family="sans-serif" font-size="24" font-weight="900" fill="#FFFFFF">6 Done</text>
        <text x="215" y="85" font-family="sans-serif" font-size="11" font-weight="600" fill="#38BDF8">Target: 8 sessions</text>

        <rect x="390" y="0" width="170" height="95" rx="14" fill="#15262B" />
        <text x="410" y="32" font-family="sans-serif" font-size="12" font-weight="700" fill="#94A3B8">STUDY STREAK</text>
        <text x="410" y="68" font-family="sans-serif" font-size="24" font-weight="900" fill="#F59E0B">🔥 5 Days</text>
        <text x="410" y="85" font-family="sans-serif" font-size="11" font-weight="600" fill="#FCD34D">Personal Record!</text>
      </g>
    </g>
  </g>
`;

// Screen 8: Tuition Fee Calculator & Faculty Directory
const screen8 = `
  <rect width="688" height="1255" fill="#F8FAFC" />
  <g transform="translate(36, 20)">
    <text x="0" y="32" font-family="sans-serif" font-size="26" font-weight="900" fill="#0F172A">Tuition &amp; Faculty Directory</text>
    <text x="0" y="58" font-family="sans-serif" font-size="14" font-weight="600" fill="#64748B">Campus Utilities: Fee Breakdown &amp; Teacher Contacts</text>

    <!-- Tuition Fee Calculator Card -->
    <g transform="translate(0, 80)">
      <rect x="0" y="0" width="616" height="175" rx="22" fill="#065F46" />
      <text x="28" y="36" font-family="sans-serif" font-size="13" font-weight="800" fill="#A7F3D0" letter-spacing="1">SEMESTER TUITION BREAKDOWN</text>

      <text x="28" y="90" font-family="sans-serif" font-size="44" font-weight="900" fill="#FFFFFF">$1,420</text>
      <text x="185" y="84" font-family="sans-serif" font-size="16" font-weight="700" fill="#6EE7B7">Net Payable</text>

      <rect x="28" y="115" width="220" height="34" rx="8" fill="#047857" />
      <text x="40" y="137" font-family="sans-serif" font-size="12" font-weight="800" fill="#D1FAE5">15 Credits • 20% Merit Waiver</text>

      <rect x="260" y="115" width="230" height="34" rx="8" fill="#047857" />
      <text x="272" y="137" font-family="sans-serif" font-size="12" font-weight="800" fill="#D1FAE5">1st Installment Paid • 2nd Nov 15</text>
    </g>

    <!-- Faculty Info Directory Section -->
    <g transform="translate(0, 280)">
      <text x="0" y="24" font-family="sans-serif" font-size="20" font-weight="900" fill="#1E293B">Faculty Directory</text>

      <!-- Faculty Member 1 -->
      <g transform="translate(0, 42)">
        <rect x="0" y="0" width="616" height="140" rx="20" fill="#FFFFFF" stroke="#E2E8F0" stroke-width="2" />
        <circle cx="56" cy="70" r="32" fill="#EFF6FF" />
        <text x="56" y="78" font-family="sans-serif" font-size="28" text-anchor="middle">👩‍🏫</text>

        <text x="108" y="48" font-family="sans-serif" font-size="19" font-weight="900" fill="#1E293B">Dr. Sarah Jenkins</text>
        <text x="108" y="72" font-family="sans-serif" font-size="13" font-weight="700" fill="#2563EB">Professor &amp; Head, Dept of CSE</text>
        <text x="108" y="96" font-family="sans-serif" font-size="13" font-weight="600" fill="#64748B">📍 Room 412 • ✉️ sjenkins@university.edu</text>
        <text x="108" y="120" font-family="sans-serif" font-size="12" font-weight="700" fill="#0D9488">Consultation: Mon &amp; Wed (02:00 PM – 04:00 PM)</text>
      </g>

      <!-- Faculty Member 2 -->
      <g transform="translate(0, 202)">
        <rect x="0" y="0" width="616" height="140" rx="20" fill="#FFFFFF" stroke="#E2E8F0" stroke-width="2" />
        <circle cx="56" cy="70" r="32" fill="#F3E8FF" />
        <text x="56" y="78" font-family="sans-serif" font-size="28" text-anchor="middle">👨‍🏫</text>

        <text x="108" y="48" font-family="sans-serif" font-size="19" font-weight="900" fill="#1E293B">Dr. Robert Vance</text>
        <text x="108" y="72" font-family="sans-serif" font-size="13" font-weight="700" fill="#7E22CE">Associate Professor, Mathematics</text>
        <text x="108" y="96" font-family="sans-serif" font-size="13" font-weight="600" fill="#64748B">📍 Room 208 • ✉️ rvance@university.edu</text>
        <text x="108" y="120" font-family="sans-serif" font-size="12" font-weight="700" fill="#0D9488">Consultation: Sun &amp; Tue (11:00 AM – 01:00 PM)</text>
      </g>
    </g>
  </g>
`;

const screenshotsConfig = [
  {
    name: 'screenshot_1_dashboard.png',
    title: 'Academic Command Center',
    subtitle: 'Daily routine, assignments & quick tools in one hub',
    badgeText: 'Academic Dashboard',
    badgeColor: '#0F766E',
    content: screen1,
    isDark: false
  },
  {
    name: 'screenshot_2_routine.png',
    title: 'Smart Class Routine',
    subtitle: 'Weekly timetable with lecture timings, rooms & teachers',
    badgeText: 'Routine & Schedule',
    badgeColor: '#2563EB',
    content: screen2,
    isDark: false
  },
  {
    name: 'screenshot_3_assignments_exams.png',
    title: 'Assignments & Exam Countdown',
    subtitle: 'Track urgent submission deadlines & exam dates',
    badgeText: 'Tasks & Deadlines',
    badgeColor: '#DC2626',
    content: screen3,
    isDark: false
  },
  {
    name: 'screenshot_4_cgpa_calculator.png',
    title: 'Accurate CGPA Calculator',
    subtitle: 'Semester grade tracker, credits & target GPA predictor',
    badgeText: 'Grade Calculator',
    badgeColor: '#0D9488',
    content: screen4,
    isDark: false
  },
  {
    name: 'screenshot_5_cover_page_generator.png',
    title: 'Instant Cover Page Generator',
    subtitle: 'Generate university assignment cover pages with PDF export',
    badgeText: 'Exclusive Feature',
    badgeColor: '#4338CA',
    content: screen5,
    isDark: false
  },
  {
    name: 'screenshot_6_lecture_notes.png',
    title: 'Course Notes & Board Photos',
    subtitle: 'Organized by topics with whiteboard snaps & classmate sharing',
    badgeText: 'Lecture Notes',
    badgeColor: '#0284C7',
    content: screen6,
    isDark: false
  },
  {
    name: 'screenshot_7_focus_timer.png',
    title: 'Deep Work Focus Timer',
    subtitle: 'Pomodoro study sessions, break intervals & productivity streak',
    badgeText: 'Productivity Timer',
    badgeColor: '#F59E0B',
    content: screen7,
    isDark: true
  },
  {
    name: 'screenshot_8_tuition_faculty.png',
    title: 'Tuition & Faculty Directory',
    subtitle: 'Credit waiver calculator & professors consultation directory',
    badgeText: 'Campus Utilities',
    badgeColor: '#059669',
    content: screen8,
    isDark: false
  }
];

function getScreenshotSvg(config) {
  const bgGrad = config.isDark
    ? `<linearGradient id="bg_${config.name}" x1="0%" y1="0%" x2="0%" y2="100%">
        <stop offset="0%" stop-color="#091418" />
        <stop offset="35%" stop-color="#0F2426" />
        <stop offset="100%" stop-color="#060C0E" />
       </linearGradient>`
    : `<linearGradient id="bg_${config.name}" x1="0%" y1="0%" x2="0%" y2="100%">
        <stop offset="0%" stop-color="#0F172A" />
        <stop offset="30%" stop-color="#1E293B" />
        <stop offset="100%" stop-color="#0F172A" />
       </linearGradient>`;

  return `
<svg width="1080" height="1920" viewBox="0 0 1080 1920" xmlns="http://www.w3.org/2000/svg">
  <defs>
    ${bgGrad}
    <filter id="phoneShadow" x="-20%" y="-20%" width="140%" height="140%">
      <feGaussianBlur stdDeviation="25" result="blur" />
      <feColorMatrix type="matrix" values="0 0 0 0 0   0 0 0 0 0   0 0 0 0 0  0 0 0 0.5 0"/>
    </filter>
    <filter id="topGlow" x="-30%" y="-30%" width="160%" height="160%">
      <feGaussianBlur stdDeviation="60" result="blur" />
      <feComposite in="SourceGraphic" in2="blur" operator="over" />
    </filter>
  </defs>

  <!-- Background Base -->
  <rect width="1080" height="1920" fill="url(#bg_${config.name})" />
  
  <!-- Subtle Ambient Glow -->
  <circle cx="540" cy="180" r="340" fill="${config.badgeColor}" opacity="0.22" filter="url(#topGlow)" />

  <!-- Top Marketing Typography -->
  <g transform="translate(540, 150)">
    <!-- Pill Badge -->
    <rect x="-130" y="0" width="260" height="42" rx="21" fill="${config.badgeColor}" opacity="0.9" />
    <text x="0" y="27" font-family="'Inter', -apple-system, sans-serif" font-size="15" font-weight="800" fill="#FFFFFF" text-anchor="middle" letter-spacing="1">${config.badgeText.toUpperCase()}</text>

    <!-- Main Screenshot Headline -->
    <text x="0" y="105" font-family="'Inter', -apple-system, sans-serif" font-size="52" font-weight="900" fill="#FFFFFF" text-anchor="middle" letter-spacing="-1">${config.title}</text>

    <!-- Subtitle Description -->
    <text x="0" y="155" font-family="'Inter', -apple-system, sans-serif" font-size="23" font-weight="500" fill="#94A3B8" text-anchor="middle">${config.subtitle}</text>
  </g>

  <!-- Phone Mockup Frame with Screen Content -->
  ${getPhoneMockup(config.content, config.isDark)}
</svg>
  `;
}

async function run() {
  console.log('Generating ClassMate Play Store Visual Assets...');

  // 1. Feature Graphic (1024 x 500)
  console.log('Rendering Feature Graphic (1024x500)...');
  const featSvg = getFeatureGraphicSvg();
  await sharp(Buffer.from(featSvg))
    .png({ quality: 100 })
    .toFile(path.join(outDir, 'feature_graphic_1024x500.png'));

  // 2. High-Res App Icon (512 x 512)
  console.log('Rendering App Icon (512x512)...');
  const iconSvg = getAppIconSvg();
  await sharp(Buffer.from(iconSvg))
    .png({ quality: 100 })
    .toFile(path.join(outDir, 'app_icon_512x512.png'));

  // 3. 8 Screenshots (1080 x 1920)
  for (let i = 0; i < screenshotsConfig.length; i++) {
    const sc = screenshotsConfig[i];
    console.log(`Rendering Screenshot ${i + 1}/${screenshotsConfig.length}: ${sc.name}...`);
    const svgStr = getScreenshotSvg(sc);
    await sharp(Buffer.from(svgStr))
      .png({ quality: 100 })
      .toFile(path.join(outDir, sc.name));
  }

  console.log('All 10 assets generated successfully!');
}

run().catch(err => {
  console.error('Asset Generation Error:', err);
  process.exit(1);
});
