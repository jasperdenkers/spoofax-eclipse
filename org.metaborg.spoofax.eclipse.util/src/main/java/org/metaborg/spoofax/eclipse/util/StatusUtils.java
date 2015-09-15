package org.metaborg.spoofax.eclipse.util;

import org.eclipse.core.resources.IResourceStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

/**
 * Utility functions for creating {@link IStatus} instances.
 */
public final class StatusUtils {
    private static final String ID = "org.metaborg.spoofax.eclipse";

    public static ValueStatus value(Object value) {
        return new ValueStatus(value);
    }


    public static IStatus status(int severity, int code, String message, Throwable t) {
        return new Status(severity, ID, code, message, t);
    }

    public static IStatus status(int severity, int code, String message) {
        return status(severity, code, message, null);
    }

    public static IStatus status(int severity, int code) {
        return status(severity, code, "");
    }


    public static IStatus success() {
        return status(IStatus.OK, IStatus.OK);
    }

    public static IStatus success(String message) {
        return status(IStatus.OK, IStatus.OK, message);
    }


    public static IStatus cancel() {
        return status(IStatus.CANCEL, IStatus.CANCEL);
    }

    public static IStatus cancel(String message) {
        return status(IStatus.CANCEL, IStatus.CANCEL, message);
    }


    public static IStatus info(String message) {
        return status(IStatus.INFO, IStatus.ERROR, message);
    }


    public static IStatus warn(String message) {
        return status(IStatus.WARNING, IStatus.ERROR, message);
    }

    public static IStatus warn(Throwable t) {
        return status(IStatus.WARNING, IStatus.ERROR, "", t);
    }

    public static IStatus warn(String message, Throwable t) {
        return status(IStatus.WARNING, IStatus.ERROR, message, t);
    }


    public static IStatus silentError() {
        return status(IStatus.OK, IStatus.ERROR);
    }

    public static IStatus silentError(String message) {
        return status(IStatus.OK, IStatus.ERROR, message);
    }

    public static IStatus silentError(Throwable t) {
        return status(IStatus.OK, IStatus.ERROR, "", t);
    }

    public static IStatus silentError(String message, Throwable t) {
        return status(IStatus.OK, IStatus.ERROR, message, t);
    }


    public static IStatus error() {
        return status(IStatus.ERROR, IStatus.ERROR);
    }

    public static IStatus error(String message) {
        return status(IStatus.ERROR, IStatus.ERROR, message);
    }

    public static IStatus error(Throwable t) {
        return status(IStatus.ERROR, IStatus.ERROR, "", t);
    }

    public static IStatus error(String message, Throwable t) {
        return status(IStatus.ERROR, IStatus.ERROR, message, t);
    }


    public static IStatus buildFailure() {
        return status(IStatus.ERROR, IResourceStatus.BUILD_FAILED);
    }

    public static IStatus buildFailure(String message) {
        return status(IStatus.ERROR, IResourceStatus.BUILD_FAILED, message);
    }

    public static IStatus buildFailure(Throwable t) {
        return status(IStatus.ERROR, IResourceStatus.BUILD_FAILED, "", t);
    }

    public static IStatus buildFailure(String message, Throwable t) {
        return status(IStatus.ERROR, IResourceStatus.BUILD_FAILED, message, t);
    }
}
