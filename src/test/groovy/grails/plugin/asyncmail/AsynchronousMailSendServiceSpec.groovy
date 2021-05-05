package grails.plugin.asyncmail

import grails.plugins.mail.MailMessageBuilder
import grails.plugins.mail.MailService
import grails.testing.services.ServiceUnitTest
import spock.lang.Ignore
import spock.lang.Specification

/**
 * Test for synchornous send service
 */
class AsynchronousMailSendServiceSpec extends Specification implements ServiceUnitTest<AsynchronousMailSendService> {
    void setup() {
        service.mailService = Mock(MailService)
    }

    @Ignore
    def "test send"() {
        given: "a message"
            AsynchronousMailMessage message = new AsynchronousMailMessage(
                    from: 'John Smith <john@example.com>',
                    to: ['Mary Smith <mary@example.com>'],
                    subject: 'Subject',
                    text: 'Text'
            )
        when: "send"
            service.send(message)
        then: "calls mailService.sendMail() method"
            1 * service.mailService.sendMail(_ as Closure)
    }

    @Ignore
    def "test text alternative and multipart"() {
        given: 'a message with alternative'
            AsynchronousMailMessage message = new AsynchronousMailMessage(
                    from: 'John Smith <john@example.com>',
                    to: ['Mary Smith <mary@example.com>'],
                    subject: 'Subject',
                    text: '<html>HTML text</html>',
                    html: true,
                    alternative: 'Alternative text'
            )
        and: 'stub method sendMail'
            def mockMessageBuilder = Mock(MailMessageBuilder) {
                isMimeCapable() >> true
            }
            service.mailService.sendMail(_ as Closure) >> { Closure cl ->
                cl.delegate = mockMessageBuilder
                cl.call()
            }
        when: "send"
            service.send(message)
        then: 'call mailService with multipart'
            1 * mockMessageBuilder.multipart(true)
    }
}
