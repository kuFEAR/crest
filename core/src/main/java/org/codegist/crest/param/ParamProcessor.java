/*
 * Copyright 2011 CodeGist.org
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 *
 *  ===================================================================
 *
 *  More information at http://www.codegist.org.
 */

package org.codegist.crest.param;

import java.nio.charset.Charset;
import java.util.List;

/**
 * <p>Param processor are used to transform a {@link org.codegist.crest.param.Param} into a list of pre-encoded pairs ready to be consumed.</p>
 * @author laurent.gilles@codegist.org
 */
public interface ParamProcessor {

    /**
     * Tranforms the given param into a ready-to-consume list of encoded pairs.
     * @param param parameter to process
     * @param charset charset to use if applicable
     * @param encodeIfNeeded if set to false, will turn off auto-encoding for parameter that needs it
     * @return the list of pre-encoded parameters
     * @throws Exception Any exception thrown during parameter processing
     */
    List<EncodedPair> process(Param param, Charset charset, boolean encodeIfNeeded) throws Exception;

}
