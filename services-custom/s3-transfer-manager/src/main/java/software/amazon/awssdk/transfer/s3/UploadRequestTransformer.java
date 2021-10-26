/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package software.amazon.awssdk.transfer.s3;

import software.amazon.awssdk.annotations.SdkPreviewApi;
import software.amazon.awssdk.annotations.SdkPublicApi;
import software.amazon.awssdk.annotations.ThreadSafe;

/**
 * A transformer for modifying {@link UploadRequest}s. This is commonly used as a functional interface in an upload request via
 * {@link UploadDirectoryRequest.Builder#uploadRequestTransformer(UploadRequestTransformer)}.
 */
@FunctionalInterface
@ThreadSafe
@SdkPublicApi
@SdkPreviewApi
public interface UploadRequestTransformer {
    /**
     * Transform an {@link UploadRequest}.
     *
     * @param originalRequest The request to transform
     * @return The transformed request.
     */
    UploadRequest transform(UploadRequest originalRequest);

    /**
     * Return an implementation of {@link UploadRequestTransformer} that returns the input request without modification.
     */
    static UploadRequestTransformer identity() {
        return r -> r;
    }
}
