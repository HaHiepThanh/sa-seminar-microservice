FROM nginx:alpine

COPY ./nginx.conf /etc/nginx/nginx.conf

COPY ./ecom-fe /usr/share/nginx/html
