/*
 * Copyright 2010 CodeGist.org
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

package org.codegist.crest.param.common;

import org.codegist.crest.model.BunchOfData;
import org.codegist.crest.model.Data;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public interface Params {

    String send(String p1, int p2);

    String dates(Date p1, Date... p2);

    String nulls(String p1, Collection<String> p2, String[] p3);

    String nullsMerging(String p1,Collection<String> p2, String[] p3);

    String defaultValue(String p1, Integer p2);
    
    String defaultParams(String p1);
    
    String defaultLists(String[] p1, boolean[] p2, List<Integer> p3, Set<Long> p4);

    String mergingLists(String[] p1, boolean[] p2, List<Integer> p3, Set<Long> p4);

    String encodings(String p1, Collection<String> p2);

    String preEncoded(String p1, Collection<String> p2);

    String defaultSerialize(Data p1, Collection<BunchOfData<Data>> p2, BunchOfData<Data>[] p3);

    String configuredSerialize(Data p1, Collection<BunchOfData<Data>> p2, BunchOfData<Data>[] p3);

    String serializeNulls(Data p1, Collection<BunchOfData<Data>> p2, BunchOfData<Data>[] p3);
}
