"""This is a predefined exposed Pyro Object that initializes and registers new services"""
import logging
import Pyro4
from service_provider import ServiceProvider

logging.basicConfig(level=logging.INFO)


@Pyro4.expose
class PyroObjectCreator(object):
    @Pyro4.oneway
    def create_new_pyro(self, name):
        temp_daemon = Pyro4.Daemon()
        ns = Pyro4.locateNS()
        uri = temp_daemon.register(ServiceProvider)
        logging.info("Creating new {0}".format(uri))
        ns.register(name, uri)
        temp_daemon.requestLoop()

        return 0


daemon = Pyro4.Daemon()
ns = Pyro4.locateNS()
uri = daemon.register(PyroObjectCreator)
ns.register("pyroobjectcreator", uri)
daemon.requestLoop()
logging.info("Pyro object creator ready {0}".format(uri))
