package at.crimsonbit.nodesystem.util;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ThreadPool {
	private static ThreadPool instance = null;
	private ExecutorService pool;
	private int threads;

	public static ThreadPool getThreadPool() {
		if (instance == null)
			instance = new ThreadPool();
		return instance;
	}

	public static void shutdown() {
		if (instance != null) {
			instance.pool.shutdown();
			instance = null;
		}
	}

	private ThreadPool() {
		threads = Math.max(Runtime.getRuntime().availableProcessors() / 2, 1);
		pool = Executors.newFixedThreadPool(threads);
	}

	/**
	 * Get the underlying pool of this ThreadPool
	 * 
	 * @return
	 */
	public ExecutorService getPool() {
		return pool;
	}

	/**
	 * Get the number of Threads in the Pool of this ThreadPool
	 * 
	 * @return
	 */
	public int getThreads() {
		return threads;
	}

	/**
	 * Submits a value-returning task for execution and returns a Future
	 * representing the pending results of the task. The Future's {@code get} method
	 * will return the task's result upon successful completion.
	 *
	 * <p>
	 * If you would like to immediately block waiting for a task, you can use
	 * constructions of the form {@code result = exec.submit(aCallable).get();}
	 *
	 * <p>
	 * Note: The {@link Executors} class includes a set of methods that can convert
	 * some other common closure-like objects, for example,
	 * {@link java.security.PrivilegedAction} to {@link Callable} form so they can
	 * be submitted.
	 *
	 * @param task
	 *            the task to submit
	 * @param <T>
	 *            the type of the task's result
	 * @return a Future representing pending completion of the task
	 * @throws RejectedExecutionException
	 *             if the task cannot be scheduled for execution
	 * @throws NullPointerException
	 *             if the task is null
	 */
	<T> Future<T> submit(Callable<T> task) {
		return pool.submit(task);
	}

	/**
	 * Submits a Runnable task for execution and returns a Future representing that
	 * task. The Future's {@code get} method will return the given result upon
	 * successful completion.
	 *
	 * @param task
	 *            the task to submit
	 * @param result
	 *            the result to return
	 * @param <T>
	 *            the type of the result
	 * @return a Future representing pending completion of the task
	 * @throws RejectedExecutionException
	 *             if the task cannot be scheduled for execution
	 * @throws NullPointerException
	 *             if the task is null
	 */
	<T> Future<T> submit(Runnable task, T result) {
		return pool.submit(task, result);
	}

	/**
	 * Submits a Runnable task for execution and returns a Future representing that
	 * task. The Future's {@code get} method will return {@code null} upon
	 * <em>successful</em> completion.
	 *
	 * @param task
	 *            the task to submit
	 * @return a Future representing pending completion of the task
	 * @throws RejectedExecutionException
	 *             if the task cannot be scheduled for execution
	 * @throws NullPointerException
	 *             if the task is null
	 */
	Future<?> submit(Runnable task) {
		return pool.submit(task);
	}

	/**
	 * Executes the given tasks, returning a list of Futures holding their status
	 * and results when all complete. {@link Future#isDone} is {@code true} for each
	 * element of the returned list. Note that a <em>completed</em> task could have
	 * terminated either normally or by throwing an exception. The results of this
	 * method are undefined if the given collection is modified while this operation
	 * is in progress.
	 *
	 * @param tasks
	 *            the collection of tasks
	 * @param <T>
	 *            the type of the values returned from the tasks
	 * @return a list of Futures representing the tasks, in the same sequential
	 *         order as produced by the iterator for the given task list, each of
	 *         which has completed
	 * @throws InterruptedException
	 *             if interrupted while waiting, in which case unfinished tasks are
	 *             cancelled
	 * @throws NullPointerException
	 *             if tasks or any of its elements are {@code null}
	 * @throws RejectedExecutionException
	 *             if any task cannot be scheduled for execution
	 */
	<T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
		return pool.invokeAll(tasks);
	}

	/**
	 * Executes the given tasks, returning a list of Futures holding their status
	 * and results when all complete or the timeout expires, whichever happens
	 * first. {@link Future#isDone} is {@code true} for each element of the returned
	 * list. Upon return, tasks that have not completed are cancelled. Note that a
	 * <em>completed</em> task could have terminated either normally or by throwing
	 * an exception. The results of this method are undefined if the given
	 * collection is modified while this operation is in progress.
	 *
	 * @param tasks
	 *            the collection of tasks
	 * @param timeout
	 *            the maximum time to wait
	 * @param unit
	 *            the time unit of the timeout argument
	 * @param <T>
	 *            the type of the values returned from the tasks
	 * @return a list of Futures representing the tasks, in the same sequential
	 *         order as produced by the iterator for the given task list. If the
	 *         operation did not time out, each task will have completed. If it did
	 *         time out, some of these tasks will not have completed.
	 * @throws InterruptedException
	 *             if interrupted while waiting, in which case unfinished tasks are
	 *             cancelled
	 * @throws NullPointerException
	 *             if tasks, any of its elements, or unit are {@code null}
	 * @throws RejectedExecutionException
	 *             if any task cannot be scheduled for execution
	 */
	<T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
			throws InterruptedException {
		return pool.invokeAll(tasks, timeout, unit);
	}

	/**
	 * Executes the given tasks, returning the result of one that has completed
	 * successfully (i.e., without throwing an exception), if any do. Upon normal or
	 * exceptional return, tasks that have not completed are cancelled. The results
	 * of this method are undefined if the given collection is modified while this
	 * operation is in progress.
	 *
	 * @param tasks
	 *            the collection of tasks
	 * @param <T>
	 *            the type of the values returned from the tasks
	 * @return the result returned by one of the tasks
	 * @throws InterruptedException
	 *             if interrupted while waiting
	 * @throws NullPointerException
	 *             if tasks or any element task subject to execution is {@code null}
	 * @throws IllegalArgumentException
	 *             if tasks is empty
	 * @throws ExecutionException
	 *             if no task successfully completes
	 * @throws RejectedExecutionException
	 *             if tasks cannot be scheduled for execution
	 */
	<T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
		return pool.invokeAny(tasks);
	}

	/**
	 * Executes the given tasks, returning the result of one that has completed
	 * successfully (i.e., without throwing an exception), if any do before the
	 * given timeout elapses. Upon normal or exceptional return, tasks that have not
	 * completed are cancelled. The results of this method are undefined if the
	 * given collection is modified while this operation is in progress.
	 *
	 * @param tasks
	 *            the collection of tasks
	 * @param timeout
	 *            the maximum time to wait
	 * @param unit
	 *            the time unit of the timeout argument
	 * @param <T>
	 *            the type of the values returned from the tasks
	 * @return the result returned by one of the tasks
	 * @throws InterruptedException
	 *             if interrupted while waiting
	 * @throws NullPointerException
	 *             if tasks, or unit, or any element task subject to execution is
	 *             {@code null}
	 * @throws TimeoutException
	 *             if the given timeout elapses before any task successfully
	 *             completes
	 * @throws ExecutionException
	 *             if no task successfully completes
	 * @throws RejectedExecutionException
	 *             if tasks cannot be scheduled for execution
	 */
	<T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
			throws InterruptedException, ExecutionException, TimeoutException {
		return pool.invokeAny(tasks, timeout, unit);
	}

}
