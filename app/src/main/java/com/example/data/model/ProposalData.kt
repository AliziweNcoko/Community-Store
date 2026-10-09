package com.example.data.model

data class ProjectPhase(
  val phaseNumber: Int,
  val title: String,
  val months: String,
  val objectives: List<String>,
  val deliverables: List<String>
)

data class ProjectRole(
  val title: String,
  val responsibilities: String,
  val icon: String
)

data class ChallengeSolution(
  val challengeTitle: String,
  val problemDescription: String,
  val solutions: List<String>
)

object CommunityStoreProposal {
  val summary = "A mobile-first marketplace designed to connect students, faculty, local vendors, and residents across South African university campuses. It provides a trusted ecosystem for buying, selling, and trading goods and services while promoting affordability, sustainability, and community engagement."

  val phases = listOf(
    ProjectPhase(
      phaseNumber = 1,
      title = "Phase 1: Discovery & Planning",
      months = "Months 1 - 2",
      objectives = listOf(
        "Engage campus student representative councils (SRCs), faculty deans, and local merchant associations.",
        "Synthesize requirements for multi-role identity verification (.ac.za and CIPC integration).",
        "Establish security architecture for South African payment gateways (PayFast, SnapScan)."
      ),
      deliverables = listOf(
        "Stakeholder Engagement Report",
        "Initial Risk Register & Governance Matrix",
        "System Architecture & Database ERD Specifications"
      )
    ),
    ProjectPhase(
      phaseNumber = 2,
      title = "Phase 2: Design & Development Management",
      months = "Months 3 - 5",
      objectives = listOf(
        "Iterative bi-weekly sprints developing Jetpack Compose Android client and reactive microservices.",
        "Implement campus safe-zone escrow protection and PayFast/SnapScan checkout pipelines.",
        "Build community bulletin board, eco-impact tracking, and zero-trust rating mechanisms."
      ),
      deliverables = listOf(
        "Sprint documentation & velocity reports",
        "Verified seller onboarding module",
        "Community Bulletin Board & Escrow API integration"
      )
    ),
    ProjectPhase(
      phaseNumber = 3,
      title = "Phase 3: Testing, Deployment & Closure",
      months = "Months 6 - 7",
      objectives = listOf(
        "Alpha launch with student unions at UCT, Wits, and Stellenbosch.",
        "Security penetration testing on escrow flows and fraud detection pipelines.",
        "Full pilot deployment, merchant expansion, and final academic project evaluation."
      ),
      deliverables = listOf(
        "QA test suite & Penetration testing sign-off",
        "Play Store production release",
        "Final Project Evaluation & Lessons Learned Register"
      )
    )
  )

  val teamRoles = listOf(
    ProjectRole(
      title = "Project Manager",
      responsibilities = "Oversees 7-month timeline, milestone deliverables, SRC and merchant stakeholder communication, sprint cadences, and budget governance.",
      icon = "assignment_ind"
    ),
    ProjectRole(
      title = "Backend Developers",
      responsibilities = "Develops high-throughput microservices, database schemas, university SSO authentication, and PayFast/SnapScan payment webhooks.",
      icon = "dns"
    ),
    ProjectRole(
      title = "Frontend Developers",
      responsibilities = "Architects modern Jetpack Compose mobile app, responsive layouts, accessible UI components, and real-time state synchronization.",
      icon = "smartphone"
    ),
    ProjectRole(
      title = "QA Engineers",
      responsibilities = "Conducts automated unit tests, regression suites, user journey validations, and stress testing during campus peak cycles (O-Week, exam prep).",
      icon = "fact_check"
    ),
    ProjectRole(
      title = "Security Specialist",
      responsibilities = "Enforces payment safety, cryptographic storage, automated fraud pattern detection, and university email verification integrity.",
      icon = "security"
    ),
    ProjectRole(
      title = "Community Liaison",
      responsibilities = "Drives local vendor onboarding (CIPC compliance), student club partnerships, safe-zone signage installation, and resident outreach.",
      icon = "handshake"
    )
  )

  val challengesAndSolutions = listOf(
    ChallengeSolution(
      challengeTitle = "Trust & Safety",
      problemDescription = "Risk of counterfeit products, misleading descriptions, or dishonest sellers eroding marketplace confidence.",
      solutions = listOf(
        "Enforce verified credentials: students and faculty via .ac.za university domain; vendors via CIPC business registration.",
        "Community-driven rating and feedback loops tied to verified completed transactions.",
        "Designated CCTV-monitored Campus Safe Meeting Zones with campus security.",
        "AI-assisted fraud detection monitoring price anomalies and abnormal transaction patterns."
      )
    ),
    ChallengeSolution(
      challengeTitle = "Payment Security",
      problemDescription = "Handling financial exchanges exposes users to non-delivery, payment fraud, and unauthorized chargebacks.",
      solutions = listOf(
        "Native integration with trusted South African payment gateways: PayFast (Instant EFT) and SnapScan QR.",
        "Campus Safe Escrow protocol: funds are locked until buyer inspects the goods at a safe zone and confirms release.",
        "Biometric authentication and two-factor checks for high-value purchases (laptops, lab equipment).",
        "End-to-end tokenized transactions with zero on-device card storage."
      )
    ),
    ChallengeSolution(
      challengeTitle = "Adoption & Engagement",
      problemDescription = "Overcoming user habituation toward unstructured WhatsApp groups or informal social media pages.",
      solutions = listOf(
        "Zero-fee student listings, referral credits, and verified seller badges.",
        "Partnerships with student unions (SRC) during Orientation Week textbook rush.",
        "Gamified sustainability scores: reward badges and leaderboards for kilograms of CO2 and waste saved.",
        "Exclusive student discounts negotiated with local campus cafes and bookstores."
      )
    ),
    ChallengeSolution(
      challengeTitle = "Scalability & Performance",
      problemDescription = "Marketplace spikes during semester starts and exam periods causing server slowdowns.",
      solutions = listOf(
        "Microservices architecture with auto-scaling containerized cloud backends.",
        "Redis caching layers for instant search autocomplete and localized campus filters.",
        "Edge-cached product thumbnails and image optimization.",
        "Real-time health monitoring with automatic failover."
      )
    ),
    ChallengeSolution(
      challengeTitle = "Community Engagement",
      problemDescription = "Ensuring the platform functions as an enriching social ecosystem rather than merely a sterile transactional utility.",
      solutions = listOf(
        "Integrated Community Bulletin Board for campus events, notices, and club fundraisers.",
        "Peer-to-peer skill swaps and tutoring exchange boards.",
        "Highlighted eco-friendly upcycling and circular economy textbook trade-ins.",
        "Interactive student polls and democratic community feature requests."
      )
    ),
    ChallengeSolution(
      challengeTitle = "Project Management Risks",
      problemDescription = "Managing multi-stakeholder delivery (SRC, faculty, merchants, security) across 7 months without scope creep.",
      solutions = listOf(
        "Living Risk Register updated at each sprint review.",
        "Bi-weekly milestone demonstrations with student focus groups.",
        "Clear transparent escalation paths and sprint burndown tracking.",
        "Iterative retrospectives documenting lessons learned at each phase closure."
      )
    )
  )
}
