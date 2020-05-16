package co.early.fore.kt.core.ui

import co.early.fore.kt.core.ui.SyncTrigger.CheckTriggerThreshold
import co.early.fore.kt.core.ui.SyncTrigger.DoThisWhenTriggered

/**
 * Convenience class used to fire one-off events from within the [SyncableView.syncView]
 * method of a view.
 *
 * [DoThisWhenTriggered.triggered] is called once the first time that
 * [CheckTriggerThreshold.checkThreshold] returns true when checked.
 *
 * If [CheckTriggerThreshold.checkThreshold] later
 * returns false when checked, then the trigger is reset and will fire again if
 * [CheckTriggerThreshold.checkThreshold] once again returns true.
 *
 * To configure this trigger to immediately reset itself after each time it's fired without
 * having to wait for [CheckTriggerThreshold.checkThreshold] to return false
 * construct this class with the ResetRule.IMMEDIATELY flag
 *
 */
class SyncTrigger constructor(
    private val doThisWhenTriggered: DoThisWhenTriggered,
    private val checkTriggerThreshold: CheckTriggerThreshold,
    private val resetRule: ResetRule = ResetRule.ONLY_AFTER_REVERSION
) {

    private var overThreshold = false
    private var firstCheck = true

    enum class ResetRule {

        /*
            Trigger is reset after each successful check
         */
        IMMEDIATELY,

        /*
            Trigger is only reset after a successful check, once a subsequent check fails.
            This is the default.
         */
        ONLY_AFTER_REVERSION,

        /*
            Trigger is never reset i.e. it fires once only _per instance_. NB: SyncTriggers usually
            live in Views and are destroyed and recreated on device rotation along with the View,
            which would give you a new instance - although checkLazy() might be enough to prevent
            issues here. You might instead prefer to keep the SyncTrigger in a ViewModel to reduce
            the likely-hood of getting a new instance of the SyncTrigger.
         */
        NEVER
    }

    /**
     * If you are using this in a view that supports rotation, you might want to look at
     * [.checkLazy]
     *
     * SyncTriggers usually exist in the the view layer and as such they are completely
     * reconstructed each time a view is rotated (on Android).
     *
     * This means that if your trigger threshold is met (causing the trigger to be fired) and then
     * you rotate the screen - the first check on the newly constructed syncTrigger will also cause
     * the trigger to be fired, continually rotating the screen will result in continual trigger
     * firing.
     *
     * This is probably not what you want, so you can use checkLazy() instead which swallows
     * the first trigger IF and only if it occurs on the FIRST EVER check of the trigger
     * threshold for this syncTrigger.
     *
     */
    fun check() {
        check(false)
    }

    /**
     * A trigger check (similar to [.check] but which swallows the first trigger IF and
     * only if it occurs on the FIRST EVER check of the trigger threshold for this syncTrigger.
     *
     * SyncTriggers usually exist in the the view layer and as such they are completely
     * reconstructed each time a view is rotated (on Android).
     *
     * This means that if your trigger threshold is met (causing the trigger to be fired) and then
     * you rotate the screen - the first check on the newly constructed syncTrigger will also cause
     * the trigger to be fired, continually rotating the screen will result in continual trigger
     * firing.
     *
     * For this reason, you might want to use this method for your application
     */
    fun checkLazy() {
        check(true)
    }

    /**
     * @param swallowTriggerForFirstCheck true to swallow triggers on the first check - depending
     * on your application this maybe useful to prevent triggers
     * firing due to a screen rotation.
     */
    private fun check(swallowTriggerForFirstCheck: Boolean) {
        val reached = checkTriggerThreshold.checkThreshold()
        if (!overThreshold && reached) {
            overThreshold = true
            if (!(swallowTriggerForFirstCheck && firstCheck)) { //not ignoring the first check AND threshold has been reached
                fireTrigger()
            }
        }
        firstCheck = false
        when (resetRule) {
            ResetRule.IMMEDIATELY -> overThreshold = false
            ResetRule.ONLY_AFTER_REVERSION -> if (!reached) {
                overThreshold = false
            }
            ResetRule.NEVER -> {
            }
        }
    }

    private fun fireTrigger() {
        doThisWhenTriggered.triggered()
    }

    interface CheckTriggerThreshold {
        /**
         *
         * @return true if the threshold has been reached or requirement is met. false if not
         */
        fun checkThreshold(): Boolean
    }

    interface DoThisWhenTriggered {
        fun triggered()
    }

}