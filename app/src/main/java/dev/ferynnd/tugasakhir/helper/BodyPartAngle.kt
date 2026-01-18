package dev.ferynnd.tugasakhir.helper

import com.google.mediapipe.tasks.components.containers.NormalizedLandmark

open class BodyPartAngle(private val landmarks: List<NormalizedLandmark>) {


    fun getPoint(index: Int): PoseMath.Point {
        val lm = landmarks[index]
        return PoseMath.Point(lm.x(), lm.y())
    }

    fun angleLeftArm() = PoseMath.calculateAngle(
        getPoint(11), // LEFT_SHOULDER
        getPoint(13), // LEFT_ELBOW
        getPoint(15)  // LEFT_WRIST
    )

    fun angleRightArm() = PoseMath.calculateAngle(
        getPoint(12), // RIGHT_SHOULDER
        getPoint(14), // RIGHT_ELBOW
        getPoint(16)  // RIGHT_WRIST
    )

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

}

