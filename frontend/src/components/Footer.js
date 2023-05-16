import React from "react";
import { Container, Row, Col } from "react-bootstrap";

const Footer = () => {
  return (
    <footer>
      <Container>
        <Row>
          {/* py-3: padding on y axis */}
          <Col className="text-center py-3">Copyright &copy; Nutrition App</Col>
        </Row>
      </Container>
    </footer>
  );
};

export default Footer;
