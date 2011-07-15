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

package org.codegist.crest.server;

import org.codegist.crest.server.stubs.deserilizations.ComplexeObjectDeserializationsStub;
import org.codegist.crest.server.stubs.deserilizations.DeserializationsStub;
import org.codegist.crest.server.stubs.error.ErrorHandlersStub;
import org.codegist.crest.server.stubs.request.*;

import java.io.IOException;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class CRestServer {

    private final Server server;


    public static void main(String[] args) throws IOException {
        new Thread(){
            @Override
            public void run() {
                try {
                    CRestServer server = new CRestServer("http://localhost:8080");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        
        System.out.print("Press enter to stop");
        System.in.read();
    }

    public CRestServer(String address) throws IOException, IllegalAccessException, InstantiationException {
        address += "/crest-server";
        this.server = new JerseyServer();
        this.server.start(address,
                new org.codegist.crest.server.stubs.params.queries.BasicsStub(),
                new org.codegist.crest.server.stubs.params.queries.CollectionsStub(),
                new org.codegist.crest.server.stubs.params.queries.DatesStub(),
                new org.codegist.crest.server.stubs.params.queries.DefaultValuesStub(),
                new org.codegist.crest.server.stubs.params.queries.EncodingsStub(),
                new org.codegist.crest.server.stubs.params.queries.NullsStub(),
                new org.codegist.crest.server.stubs.params.queries.SerializersStub(),
                new org.codegist.crest.server.stubs.params.queries.SpecialsStub(),

                new org.codegist.crest.server.stubs.params.matrixes.BasicsStub(),
                new org.codegist.crest.server.stubs.params.matrixes.CollectionsStub(),
                new org.codegist.crest.server.stubs.params.matrixes.DatesStub(),
                new org.codegist.crest.server.stubs.params.matrixes.DefaultValuesStub(),
                new org.codegist.crest.server.stubs.params.matrixes.EncodingsStub(),
                new org.codegist.crest.server.stubs.params.matrixes.NullsStub(),
                new org.codegist.crest.server.stubs.params.matrixes.SerializersStub(),
                new org.codegist.crest.server.stubs.params.matrixes.SpecialsStub(),

                new org.codegist.crest.server.stubs.params.forms.BasicsStub(),
                new org.codegist.crest.server.stubs.params.forms.CollectionsStub(),
                new org.codegist.crest.server.stubs.params.forms.DatesStub(),
                new org.codegist.crest.server.stubs.params.forms.DefaultValuesStub(),
                new org.codegist.crest.server.stubs.params.forms.EncodingsStub(),
                new org.codegist.crest.server.stubs.params.forms.NullsStub(),
                new org.codegist.crest.server.stubs.params.forms.SerializersStub(),
                new org.codegist.crest.server.stubs.params.forms.SpecialsStub(),

                new org.codegist.crest.server.stubs.params.forms.json.FormJsonEntitiesStub(),
                new org.codegist.crest.server.stubs.params.forms.xml.FormXmlEntitiesStub(),

                new org.codegist.crest.server.stubs.params.headers.BasicsStub(),
                new org.codegist.crest.server.stubs.params.headers.CollectionsStub(),
                new org.codegist.crest.server.stubs.params.headers.DatesStub(),
                new org.codegist.crest.server.stubs.params.headers.DefaultValuesStub(),
                new org.codegist.crest.server.stubs.params.headers.EncodingsStub(),
                new org.codegist.crest.server.stubs.params.headers.NullsStub(),
                new org.codegist.crest.server.stubs.params.headers.SerializersStub(),
                new org.codegist.crest.server.stubs.params.headers.SpecialsStub(),

                new org.codegist.crest.server.stubs.params.multiparts.BasicsStub(),
                new org.codegist.crest.server.stubs.params.multiparts.CollectionsStub(),
                new org.codegist.crest.server.stubs.params.multiparts.DatesStub(),
                new org.codegist.crest.server.stubs.params.multiparts.DefaultValuesStub(),
                new org.codegist.crest.server.stubs.params.multiparts.EncodingsStub(),
                new org.codegist.crest.server.stubs.params.multiparts.NullsStub(),
                new org.codegist.crest.server.stubs.params.multiparts.SerializersStub(),
                new org.codegist.crest.server.stubs.params.multiparts.MiscsStub(),

                new org.codegist.crest.server.stubs.params.paths.BasicsStub(),
                new org.codegist.crest.server.stubs.params.paths.CollectionsStub(),
                new org.codegist.crest.server.stubs.params.paths.DatesStub(),
                new org.codegist.crest.server.stubs.params.paths.DefaultValuesStub(),
                new org.codegist.crest.server.stubs.params.paths.EncodingsStub(),
                new org.codegist.crest.server.stubs.params.paths.SerializersStub(),
                new org.codegist.crest.server.stubs.params.paths.SpecialsStub(),

                new org.codegist.crest.server.stubs.params.cookies.BasicsStub(),
                new org.codegist.crest.server.stubs.params.cookies.CollectionsStub(),
                new org.codegist.crest.server.stubs.params.cookies.DatesStub(),
                new org.codegist.crest.server.stubs.params.cookies.DefaultValuesStub(),
                new org.codegist.crest.server.stubs.params.cookies.EncodingsStub(),
                new org.codegist.crest.server.stubs.params.cookies.NullsStub(),
                new org.codegist.crest.server.stubs.params.cookies.SerializersStub(),
                new org.codegist.crest.server.stubs.params.cookies.SpecialsStub(),

                new GetsStub(),
                new PostsStub(),
                new PutsStub(),
                new DeletesStub(),
                new HeadsStub(),
                new OptionsStub(),
                new DeserializationsStub(),
                new ComplexeObjectDeserializationsStub(),

                new ErrorHandlersStub()
        );
    }

    public void stop(){
        server.stop();
    }
    public void stopOnExit(){
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                server.stop();
            }
        });
    }


}
