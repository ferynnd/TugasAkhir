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

    fun angleAbdomen(): Double {
        val shoulderAvg = PoseMath.Point(
            (getPoint(11).x + getPoint(12).x) / 2,
            (getPoint(11).y + getPoint(12).y) / 2
        )
        val hipAvg = PoseMath.Point(
            (getPoint(23).x + getPoint(24).x) / 2,
            (getPoint(23).y + getPoint(24).y) / 2
        )
        val kneeAvg = PoseMath.Point(
            (getPoint(25).x + getPoint(26).x) / 2,
            (getPoint(25).y + getPoint(26).y) / 2
        )
        return PoseMath.calculateAngle(shoulderAvg, hipAvg, kneeAvg)
    }
}



//import com.google.mlkit.vision.pose.PoseLandmark
//
//open class BodyPartAngle(val landmarks: List<PoseLandmark>) {
//
//    fun getPoint(landmarkType: Int): PoseMath.Point {
//        val lm = landmarks[landmarkType]
//        return PoseMath.Point(lm.position.x, lm.position.y)
//    }
//
//    fun angleLeftArm() = PoseMath.calculateAngle(
//        getPoint(PoseLandmark.LEFT_SHOULDER),
//        getPoint(PoseLandmark.LEFT_ELBOW),
//        getPoint(PoseLandmark.LEFT_WRIST)
//    )
//
//    fun angleRightArm() = PoseMath.calculateAngle(
//        getPoint(PoseLandmark.RIGHT_SHOULDER),
//        getPoint(PoseLandmark.RIGHT_ELBOW),
//        getPoint(PoseLandmark.RIGHT_WRIST)
//    )
//
//    fun angleLeftLeg() = PoseMath.calculateAngle(
//        getPoint(PoseLandmark.LEFT_HIP),
//        getPoint(PoseLandmark.LEFT_KNEE),
//        getPoint(PoseLandmark.LEFT_ANKLE)
//    )
//
//    fun angleRightLeg() = PoseMath.calculateAngle(
//        getPoint(PoseLandmark.RIGHT_HIP),
//        getPoint(PoseLandmark.RIGHT_KNEE),
//        getPoint(PoseLandmark.RIGHT_ANKLE)
//    )
//
//    fun angleAbdomen(): Double {
//        val shoulderAvg = PoseMath.Point(
//            (getPoint(PoseLandmark.LEFT_SHOULDER).x + getPoint(PoseLandmark.RIGHT_SHOULDER).x) / 2,
//            (getPoint(PoseLandmark.LEFT_SHOULDER).y + getPoint(PoseLandmark.RIGHT_SHOULDER).y) / 2
//        )
//        val hipAvg = PoseMath.Point(
//            (getPoint(PoseLandmark.LEFT_HIP).x + getPoint(PoseLandmark.RIGHT_HIP).x) / 2,
//            (getPoint(PoseLandmark.LEFT_HIP).y + getPoint(PoseLandmark.RIGHT_HIP).y) / 2
//        )
//        val kneeAvg = PoseMath.Point(
//            (getPoint(PoseLandmark.LEFT_KNEE).x + getPoint(PoseLandmark.RIGHT_KNEE).x) / 2,
//            (getPoint(PoseLandmark.LEFT_KNEE).y + getPoint(PoseLandmark.RIGHT_KNEE).y) / 2
//        )
//        return PoseMath.calculateAngle(shoulderAvg, hipAvg, kneeAvg)
//    }
//}