package dev.ferynnd.tugasakhir.helper

import com.google.mediapipe.tasks.components.containers.NormalizedLandmark

open class BodyPartAngle(val landmarks: List<NormalizedLandmark>) {

    fun getPoint(index: Int): PoseMath.Point {
        val lm = landmarks[index]
        return PoseMath.Point(lm.x(), lm.y())
    }

    // ================= ARM =================

    // Siku kiri
    fun angleLeftArm() = PoseMath.calculateAngle(
        getPoint(11), // LEFT_SHOULDER
        getPoint(13), // LEFT_ELBOW
        getPoint(15)  // LEFT_WRIST
    )

    // Siku kanan
    fun angleRightArm() = PoseMath.calculateAngle(
        getPoint(12), // RIGHT_SHOULDER
        getPoint(14), // RIGHT_ELBOW
        getPoint(16)  // RIGHT_WRIST
    )

    // Rata-rata siku
    fun angleElbow() = (angleLeftArm() + angleRightArm()) / 2


    // ================= LEG =================

    fun angleLeftLeg() = PoseMath.calculateAngle(
        getPoint(23), // LEFT_HIP
        getPoint(25), // LEFT_KNEE
        getPoint(27)  // LEFT_ANKLE
    )

    fun angleRightLeg() = PoseMath.calculateAngle(
        getPoint(24), // RIGHT_HIP
        getPoint(26), // RIGHT_KNEE
        getPoint(28)  // RIGHT_ANKLE
    )

    fun angleKnee() = (angleLeftLeg() + angleRightLeg()) / 2


    // ================= SPINE / TORSO =================

    // Garis bahu → pinggul → pergelangan kaki
    fun angleSpine() = PoseMath.calculateAngle(
        getPoint(11), // LEFT_SHOULDER
        getPoint(23), // LEFT_HIP
        getPoint(27)  // LEFT_ANKLE
    )

    // Bahu → pinggul → lutut
    fun angleTorso() = PoseMath.calculateAngle(
        getPoint(11), // LEFT_SHOULDER
        getPoint(23), // LEFT_HIP
        getPoint(25)  // LEFT_KNEE
    )


    // ================= SHOULDER =================

    // Shoulder kiri (abduction)
    fun angleLeftShoulder() = PoseMath.calculateAngle(
        getPoint(13), // LEFT_ELBOW
        getPoint(11), // LEFT_SHOULDER
        getPoint(23)  // LEFT_HIP
    )

    // Shoulder kanan
    fun angleRightShoulder() = PoseMath.calculateAngle(
        getPoint(14), // RIGHT_ELBOW
        getPoint(12), // RIGHT_SHOULDER
        getPoint(24)  // RIGHT_HIP
    )

    // Rata-rata shoulder
    fun angleShoulder() = (angleLeftShoulder() + angleRightShoulder()) / 2

    // ================= HEAD / NECK =================

    // Sudut kepala terhadap bahu (untuk cek kepala terlalu turun / naik)
    fun angleHead() = PoseMath.calculateAngle(
        getPoint(7),  // LEFT_EAR
        getPoint(11), // LEFT_SHOULDER
        getPoint(23)  // LEFT_HIP
    )

}