# RULE TREE – SIT UP STANDARD

SitUpEvaluation
│
├── 1. Cek Posisi Awal (START Position)
│     │
│     ├── IF Knee Angle 80°–110° ?
│     │      ├── NO → Output: "Posisi lutut tidak ideal" → INVALID_START
│     │      └── YES
│     │
│     ├── IF Spine Angle 150°–180° ?
│     │      ├── NO → Output: "Posisi awal tubuh tidak netral"
│     │      └── YES
│     │
│     └── IF Head Alignment 140°–180° ?
│            ├── NO → Output: "Posisi kepala tidak netral"
│            └── YES → State = READY
│
├── 2. Fase Naik (ASCENDING)
│     │
│     ├── IF Torso Angle < 140° ?
│     │      └── State → ASCENDING
│     │
│     ├── IF Torso Angle 60°–90° ?
│     │      ├── YES → Posisi Atas Tercapai
│     │      └── NO → Output: "Belum cukup naik"
│     │
│     └── IF Torso Angle < 50° ?
│            └── Warning: "Terlalu condong ke depan"
│
├── 3. Fase Atas (TOP Position)
│     │
│     ├── IF Torso Angle 60°–90° ?
│     │      └── VALID_TOP
│     │
│     └── ELSE → INVALID_TOP
│
├── 4. Fase Turun (DESCENDING)
│     │
│     ├── IF Torso Angle > 100° ?
│     │      └── State → DESCENDING
│     │
│     ├── IF Spine Angle 150°–180° ?
│     │      └── Posisi Bawah Tercapai
│     │
│     └── ELSE → Output: "Turun tidak sempurna"
│
└── 5. Validasi Repetisi
      │
      ├── IF Sequence:
      │      START → ASC → TOP → DESC → START ?
      │
      │      ├── YES → reps++
      │      └── NO → Repetisi tidak dihitung
      │
      └── IF Tidak mencapai TOP valid ?
             └── Output: "Sit-up tidak penuh"


1️⃣ Rule Posisi Awal (TOP Position)

| No | IF (Kondisi)           | Ambang             | THEN (Aksi)                | Output                    |
| -- | ---------------------- | ------------------ | -------------------------- | ------------------------- |
| P1 | Sudut siku ≥ 160°      | Elbow extension    | Posisi TOP valid           | -                         |
| P2 | Sudut bahu 35°–55°     | Arm vs torso       | Posisi lengan ideal (±45°) | -                         |
| P3 | Sudut spine 165°–185°  | Shoulder–Hip–Ankle | Tubuh lurus                | -                         |
| P4 | Sudut spine < 150°     | Shoulder–Hip–Ankle | Koreksi                    | "Pinggul terlalu turun"   |
| P5 | Sudut spine > 190°     | Shoulder–Hip–Ankle | Koreksi                    | "Pinggul terlalu naik"    |
| P6 | Sudut kepala 150°–180° | Ear–Shoulder–Hip   | Kepala netral              | -                         |
| P7 | Sudut kepala < 140°    | Ear–Shoulder–Hip   | Koreksi                    | "Kepala terlalu menunduk" |

2️⃣ Rule Fase Turun (DESCENDING)

| No  | IF (Kondisi)      | Ambang       | THEN               | Output                   |
| --- | ----------------- | ------------ | ------------------ | ------------------------ |
| P8  | Sudut siku < 150° | Dari TOP     | State → DESCENDING | -                        |
| P9  | Sudut bahu > 70°  | Arm vs torso | Koreksi            | "Lengan terlalu melebar" |
| P10 | Sudut bahu < 20°  | Arm vs torso | Koreksi            | "Lengan terlalu rapat"   |

3️⃣ Rule Fase Bawah (BOTTOM Position)

| No  | IF (Kondisi)        | Ambang        | THEN                 | Output                         |
| --- | ------------------- | ------------- | -------------------- | ------------------------------ |
| P11 | Sudut siku 80°–100° | Elbow flexion | Depth optimal (±90°) | -                              |
| P12 | Sudut siku > 110°   | Elbow flexion | Tidak valid          | "Turunkan badan lebih dalam"   |
| P13 | Sudut siku < 70°    | Elbow flexion | Peringatan           | "Terlalu dalam, risiko cedera" |

4️⃣ Rule Fase Naik (ASCENDING)

| No  | IF (Kondisi)      | Ambang         | THEN                 |
| --- | ----------------- | -------------- | -------------------- |
| P14 | Sudut siku > 110° | Dari BOTTOM    | State → ASCENDING    |
| P15 | Sudut siku ≥ 160° | Dari ASCENDING | State → TOP + reps++ |

5️⃣ Rule Validasi Repetisi

| No  | IF                                              | THEN                             |
| --- | ----------------------------------------------- | -------------------------------- |
| P16 | Siklus lengkap: TOP → DESC → BOTTOM → ASC → TOP | Tambah 1 repetisi                |
| P17 | Tidak mencapai sudut 80°–100°                   | Repetisi tidak dihitung          |
| P18 | Spine tidak dalam rentang valid saat siklus     | Repetisi ditandai tidak sempurna |
