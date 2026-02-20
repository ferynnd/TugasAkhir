package dev.ferynnd.tugasakhir.helper

import android.util.Log
import com.google.mediapipe.tasks.components.containers.NormalizedLandmark

class TypeOfExercise(landmarks: List<NormalizedLandmark>) : BodyPartAngle(landmarks) {

    fun evaluatePushUp(
        counter: Int,
        currentState: ExerciseState
    ): ExerciseEvaluation {

        val elbow = (angleLeftArm() + angleRightArm()) / 2
        val spine = angleSpine()

        var reps = counter
        var state = currentState
        var feedback: String? = null

        // 1️⃣ POSTURE VALIDATION
        val postureValid =
            elbow >= 160 &&
            spine in 165.0..185.0

        if (state == ExerciseState.WAITING_START) {

            if (postureValid) {
                state = ExerciseState.TOP
                feedback = "Posisi siap, mulai"
            } else {
                feedback = "Ambil posisi push-up yang benar"
            }

            return ExerciseEvaluation(
                reps,
                state,
                postureValid,
                false,
                feedback
            )
        }

        if (!postureValid) {
            feedback = if (spine < 150)
                "Pinggul terlalu turun"
            else
                "Pinggul terlalu naik"
        }

        // 2️⃣ RANGE OF MOTION
        val depthValid = elbow in 80.0..100.0

        if (state == ExerciseState.DESCENDING && elbow > 110) {
            feedback = "Turunkan badan lebih dalam"
        }

        // 3️⃣ FSM
        when (state) {

            ExerciseState.WAITING_START -> {
                if (postureValid) {
                    state = ExerciseState.TOP
                }
            }

            ExerciseState.TOP -> {
                if (elbow < 150) {
                    state = ExerciseState.DESCENDING
                }
            }

            ExerciseState.DESCENDING -> {
                if (elbow in 80.0..100.0) {
                    state = ExerciseState.BOTTOM
                }
            }

            ExerciseState.BOTTOM -> {
                if (elbow > 110) {
                    state = ExerciseState.ASCENDING
                }
            }

            ExerciseState.ASCENDING -> {
                if (elbow >= 160) {
                    if (depthValid || postureValid) {
                        reps++
                    }
                    state = ExerciseState.TOP
                }
            }
        }

        Log.d(
            "TypeOfExercise",
            "Elbow=$elbow | Spine=$spine | State=$state | Reps=$reps | Feedback=$feedback"
        )

        Log.d("TypeOfExercise", "reps: $reps")


        return ExerciseEvaluation(
            reps,
            state,
            postureValid,
            depthValid,
            feedback
        )
    }

    fun evaluateSitUp(
        counter: Int,
        currentState: ExerciseState
    ): ExerciseEvaluation {

        val knee = angleKnee()
        val spine = angleSpine()
        val torso = angleTorso()

        var reps = counter
        var state = currentState
        var feedback: String? = null

        // 1️⃣ POSTURE VALIDATION (START POSITION)
        val postureValid =
            knee in 80.0..110.0 &&
            spine in 150.0..180.0


        if (state == ExerciseState.WAITING_START) {

            if (postureValid) {
                state = ExerciseState.TOP
                feedback = "Posisi siap, mulai"
            } else {
                feedback = "Ambil posisi sit-up yang benar"
            }

            return ExerciseEvaluation(
                reps,
                state,
                postureValid,
                false,
                feedback
            )
        }


        if (!postureValid) {
            feedback = when {
                knee !in 80.0..110.0 ->
                    "Posisi lutut tidak ideal"
                spine < 150 ->
                    "Posisi awal tubuh tidak netral"
                else ->
                    "Postur tidak valid"
            }
        }

        // 2️⃣ RANGE OF MOTION (TOP VALIDATION)
        val depthValid = torso in 60.0..90.0

        if (state == ExerciseState.ASCENDING && torso > 100) {
            feedback = "Belum cukup naik"
        }

        if (torso < 50) {
            feedback = "Terlalu condong ke depan"
        }

        // 3️⃣ FSM (Finite State Machine)
        when (state) {

            ExerciseState.WAITING_START -> {
                if (postureValid) {
                    state = ExerciseState.TOP
                }
            }

            ExerciseState.TOP -> {
                // mulai turun
                if (torso > 100) {
                    state = ExerciseState.DESCENDING
                }
            }

            ExerciseState.DESCENDING -> {
                // kembali ke posisi bawah (START logic)
                if (spine in 150.0..180.0) {
                    state = ExerciseState.BOTTOM
                } else {
                    feedback = "Turun tidak sempurna"
                }
            }

            ExerciseState.BOTTOM -> {
                // mulai naik
                if (torso < 140) {
                    state = ExerciseState.ASCENDING
                }
            }

            ExerciseState.ASCENDING -> {
                // capai TOP
                if (torso in 60.0..90.0) {
                    if (depthValid && postureValid) {
                        reps++
                    } else {
                        feedback = "Sit-up tidak penuh"
                    }
                    state = ExerciseState.TOP
                }
            }
        }

        Log.d(
            "TypeOfExercise",
            "Knee=$knee | Spine=$spine | Torso=$torso | State=$state | Reps=$reps | Feedback=$feedback"
        )

        return ExerciseEvaluation(
            reps,
            state,
            postureValid,
            depthValid,
            feedback
        )
    }
    

    fun evaluateSquat(
        counter: Int,
        currentState: ExerciseState
    ): ExerciseEvaluation {

        val knee = angleKnee()
        val spine = angleSpine()
        val torso = angleTorso()
        val hip = angleLeftLeg() // proxy hip angle (hip-knee-ankle)

        var reps = counter
        var state = currentState
        var feedback: String? = null

        // 1️⃣ POSTURE VALIDATION (Standing / Bottom posture check)
        val postureValid =
            knee in 160.0..180.0 &&
            hip in 160.0..180.0 &&
            torso in 80.0..100.0

        if (state == ExerciseState.WAITING_START) {

            if (postureValid) {
                state = ExerciseState.TOP
                feedback = "Posisi siap, mulai"
            } else {
                feedback = "Ambil posisi squat yang benar"
            }

            return ExerciseEvaluation(
                reps,
                state,
                postureValid,
                false,
                feedback
            )
        }

        if (!postureValid) {
            feedback = when {
                torso < 55 -> "Badan terlalu condong ke depan"
                torso > 75 -> "Badan terlalu tegak"
                spine < 150 -> "Jaga punggung tetap netral"
                else -> "Postur tidak valid"
            }
        }

        // 2️⃣ RANGE OF MOTION (Depth Validation)
        val depthValid =
            knee in 80.0..100.0 &&
            hip in 80.0..100.0

        if (state == ExerciseState.DESCENDING && knee > 110) {
            feedback = "Belum mencapai kedalaman ideal"
        }

        if (state == ExerciseState.BOTTOM && !depthValid) {
            feedback = "Squat tidak mencapai 90°"
        }

        // 3️⃣ FSM (Finite State Machine)
        when (state) {

            ExerciseState.WAITING_START -> {
                if (postureValid) {
                    state = ExerciseState.TOP
                }
            }
            ExerciseState.TOP -> {
                // STANDING
                if (knee < 150) {
                    state = ExerciseState.DESCENDING
                }
            }

            ExerciseState.DESCENDING -> {
                if (knee in 80.0..100.0) {
                    state = ExerciseState.BOTTOM
                }
            }

            ExerciseState.BOTTOM -> {
                if (knee > 110) {
                    state = ExerciseState.ASCENDING
                }
            }

            ExerciseState.ASCENDING -> {
                if (knee >= 165) {
                    if (depthValid && postureValid) {
                        reps++
                    } else {
                        feedback = "Tidak berdiri sempurna"
                    }
                    state = ExerciseState.TOP
                }
            }
        }

        Log.d(
            "TypeOfExercise",
            "Knee=$knee | Hip=$hip | Torso=$torso | Spine=$spine | State=$state | Reps=$reps | Feedback=$feedback"
        )

        return ExerciseEvaluation(
            reps,
            state,
            postureValid,
            depthValid,
            feedback
        )
    }


}