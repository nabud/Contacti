//package app.contacts.com.contacts.utilities;
//
//import android.content.Context;
//import android.view.View;
//import android.view.animation.AnimationUtils;

//public class ViewAnimations {
//
//    /**
//     * Rubber Animation
//     *
//     * @param view
//     */
//    public static void rubberAnimation(View view) {
//        YoYo.with(Techniques.RubberBand).duration(700).playOn(view);
//    }
//
//    /**
//     * Fade In Animation
//     *
//     * @param view
//     */
//    public static void fadeInAnimation(View view) {
//        YoYo.with(Techniques.FadeIn).duration(2500).playOn(view);
//    }
//
//
//    public static void fadeOutAnimation(final View view) {
//        YoYo.with(Techniques.FadeIn).duration(700).withListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animator) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animator) {
//                view.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animator) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animator) {
//
//            }
//        }).playOn(view);
//    }
//
//    public static void slideInGrowFromRight(View view){
//        YoYo.with(Techniques.SlideInRight).duration(700).playOn(view);
//        YoYo.with(Techniques.ZoomIn).duration(700).playOn(view);
//    }
//
//    public static void slideOutShrinkToRight(final View view){
//        YoYo.with(Techniques.SlideOutRight).duration(700).playOn(view);
//        YoYo.with(Techniques.ZoomOut).duration(700).withListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animator) {}
//
//            @Override
//            public void onAnimationEnd(Animator animator) {
//                view.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animator) {}
//
//            @Override
//            public void onAnimationRepeat(Animator animator) {}
//        }).playOn(view);
//    }
//
//    public static void slideInGrowFromTop(View view){
//        YoYo.with(Techniques.SlideInDown).duration(800).playOn(view);
//        YoYo.with(Techniques.ZoomIn).duration(800).playOn(view);
//    }
//
//    public static void slideOutShrinkToTop(final View view){
//        YoYo.with(Techniques.SlideOutUp).duration(700).playOn(view);
//        YoYo.with(Techniques.ZoomOut).duration(700).withListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animator) {}
//
//            @Override
//            public void onAnimationEnd(Animator animator) {
//                view.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animator) {}
//
//            @Override
//            public void onAnimationRepeat(Animator animator) {}
//        }).playOn(view);
//    }
//
//
//    public static void fadeOut(final View view){
//        YoYo.with(Techniques.FadeOut).duration(700).withListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animator) {}
//
//            @Override
//            public void onAnimationEnd(Animator animator) {
//                view.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animator) {}
//
//            @Override
//            public void onAnimationRepeat(Animator animator) {}
//        }).playOn(view);
//    }
//
//    public static void fadeIn(final View view){
//        YoYo.with(Techniques.FadeIn).duration(700).withListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animator) {}
//
//            @Override
//            public void onAnimationEnd(Animator animator) {
//                view.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animator) {}
//
//            @Override
//            public void onAnimationRepeat(Animator animator) {}
//        }).playOn(view);
//    }
//
//
//    public static void viewAnimationHideGroups(Context context, View view){
//        fadeOut(view);
//        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.left_to_center));
//    }
//
//    public static void viewAnimationHideContacts(Context context, View view){
//        fadeOut(view);
//        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.right_to_center));
//    }
//
//
//    public static void viewAnimationShowGroups(Context context, View view){
//        fadeIn(view);
//        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.center_to_left));
//    }
//
//    public static void viewAnimationShowContacts(Context context, View view){
//        fadeIn(view);
//        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.center_to_right));
//    }
//}
