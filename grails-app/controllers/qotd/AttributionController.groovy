package qotd

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class AttributionController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Attribution.list(params), model:[attributionCount: Attribution.count()]
    }

    def show(Attribution attribution) {
        respond attribution
    }

    def create() {
        respond new Attribution(params)
    }

    @Transactional
    def save(Attribution attribution) {
        if (attribution == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (attribution.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond attribution.errors, view:'create'
            return
        }

        attribution.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'attribution.label', default: 'Attribution'), attribution.id])
                redirect attribution
            }
            '*' { respond attribution, [status: CREATED] }
        }
    }

    def edit(Attribution attribution) {
        respond attribution
    }

    @Transactional
    def update(Attribution attribution) {
        if (attribution == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (attribution.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond attribution.errors, view:'edit'
            return
        }

        attribution.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'attribution.label', default: 'Attribution'), attribution.id])
                redirect attribution
            }
            '*'{ respond attribution, [status: OK] }
        }
    }

    @Transactional
    def delete(Attribution attribution) {

        if (attribution == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        attribution.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'attribution.label', default: 'Attribution'), attribution.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'attribution.label', default: 'Attribution'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
