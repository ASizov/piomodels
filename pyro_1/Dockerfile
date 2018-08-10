FROM python:3.7
MAINTAINER al <alagunas@grupoabraxas.com>

ADD . /pyro-algorithms-orax
WORKDIR /pyro-algorithms-orax

RUN pip install -r requirements.txt
RUN apt-get update && apt-get install -y supervisor

ADD supervisord.conf /etc/supervisor/conf.d/pyro-algorithms-orax.conf

ENTRYPOINT ["/usr/bin/supervisord"]
