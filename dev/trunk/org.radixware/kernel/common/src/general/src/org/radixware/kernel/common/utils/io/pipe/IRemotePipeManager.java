package org.radixware.kernel.common.utils.io.pipe;

/**
 * Interface {@link IRemotePipeManager} provides an abstraction for remote
 * proxy to {@link BidirectionalPipe}, it's implementation is bound to
 * server-specific, so placed outside of kernel.common project.
 */
public interface IRemotePipeManager {

    void connect(PipeAddress remotePipeAddress, BidirectionalPipe pipe);

    void bind(PipeAddress addr, ServerPipe server);

    void free(PipeAddress addr);

    int getInstanceId();

}