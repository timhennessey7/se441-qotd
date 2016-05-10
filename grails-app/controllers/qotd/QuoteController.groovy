package qotd

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class QuoteController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Quote.list(params), model:[quoteCount: Quote.count()]
    }

    def show(Quote quote) {
        respond quote
    }

    def create() {
        respond new Quote(params)
    }

    @Transactional
    def save(Quote quote) {
        if (quote == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (quote.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond quote.errors, view:'create'
            return
        }

        quote.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'quote.label', default: 'Quote'), quote.id])
                redirect quote
            }
            '*' { respond quote, [status: CREATED] }
        }
    }

    def edit(Quote quote) {
        respond quote
    }

    @Transactional
    def update(Quote quote) {
        if (quote == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (quote.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond quote.errors, view:'edit'
            return
        }

        quote.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'quote.label', default: 'Quote'), quote.id])
                redirect quote
            }
            '*'{ respond quote, [status: OK] }
        }
    }

    @Transactional
    def delete(Quote quote) {

        if (quote == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        quote.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'quote.label', default: 'Quote'), quote.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'quote.label', default: 'Quote'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
