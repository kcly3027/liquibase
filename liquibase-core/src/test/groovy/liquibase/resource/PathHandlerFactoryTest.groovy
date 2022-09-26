package liquibase.resource

import liquibase.Scope
import liquibase.test.JUnitResourceAccessor
import liquibase.util.StreamUtil
import spock.lang.Specification
import spock.lang.Unroll

class PathHandlerFactoryTest extends Specification {

    def "parse file path"() {
        expect:
        Scope.getCurrentScope().getSingleton(PathHandlerFactory).getResourceAccessor("src/test/groovy") instanceof DirectoryResourceAccessor
    }

    @Unroll
    def "parse unparseable file path: #input"() {
        when:
        Scope.getCurrentScope().getSingleton(PathHandlerFactory).getResourceAccessor(input) instanceof DirectoryResourceAccessor

        then:
        def e = thrown(IOException)
        e.message == "Cannot parse resource location: '$input'"

        where:
        input << [null, "proto:unsupported"]
    }

    @Unroll
    def "getResource: #path"() {
        when:
        def pathHandlerFactory = Scope.getCurrentScope().getSingleton(PathHandlerFactory)

        then:
        (pathHandlerFactory.getResource(path).exists()) == existsWithoutResourceAccessor
        Scope.child(Scope.Attr.resourceAccessor, new JUnitResourceAccessor(), { ->
            assert (pathHandlerFactory.getResource(path, true).exists()) == existsWithResourceAccessor
        })


        where:
        path                                                               | existsWithoutResourceAccessor | existsWithResourceAccessor
        "src/test/groovy/liquibase/resource/PathHandlerFactoryTest.groovy" | true                          | true
        "invalid/path.txt"                                                 | false                         | false
        "liquibase/resource/PathHandlerFactoryTest.class"                  | false                         | true
        "/liquibase/resource/PathHandlerFactoryTest.class"                 | false                         | true
    }

    def "getResource with duplicate files"() {
        when:
        def pathHandlerFactory = Scope.getCurrentScope().getSingleton(PathHandlerFactory)
        def path = "target/test-classes/duplicate.txt"

        (path as File).createNewFile()
        ("target/test-classes/" + path as File).getParentFile().mkdirs()
        ("target/test-classes/" + path as File).createNewFile()


        Scope.child(Scope.Attr.resourceAccessor, new JUnitResourceAccessor(), { ->
            pathHandlerFactory.getResource(path, true)
        })

        then:
        def e = thrown(IOException)
        e.message.startsWith("Found 2 files with the path 'target/test-classes/duplicate.txt':")
    }

    def openResourceOutputStream() {
        when:
        def tempFile = File.createTempFile("DirectoryPathHandlerTest", ".tmp")
        tempFile.deleteOnExit()
        tempFile.delete()
        def path = tempFile.getAbsolutePath()

        def pathHandlerFactory = Scope.currentScope.getSingleton(PathHandlerFactory)

        then:
        pathHandlerFactory.openResourceOutputStream(path, false, false) == null //when createIfNotExists is false

        when:
        def stream = pathHandlerFactory.openResourceOutputStream(path, false, true) //createIfNotExists is true
        stream.withWriter {
            it.write("test")
        }
        stream.close()

        then:
        StreamUtil.readStreamAsString(pathHandlerFactory.getResource(path).openInputStream()) == "test"

        when:
        //can update file
        stream = pathHandlerFactory.openResourceOutputStream(path, false, true)
        stream.withWriter {
            it.write("test 2")
        }
        stream.close()

        then:
        StreamUtil.readStreamAsString(pathHandlerFactory.getResource(path).openInputStream()) == "test 2"
    }
}
