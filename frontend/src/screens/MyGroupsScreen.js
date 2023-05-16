import React from 'react'
import { useState, useEffect } from "react";
import axios from "axios";

import ListGroup from "react-bootstrap/ListGroup";
import GroupUserInfo from '../components/GroupUserInfo';
import { Button, Col, Row } from 'react-bootstrap';

const MyGroupsScreen = () => {

  const [userGroups, setUserGroups] = useState([]);
  const userId = 5;

  useEffect(() => {
    const fetchGroups = async () => {
      try {
        const { data } = await axios.get(`/api/v1/groups/user/${userId}`);
      
        setUserGroups(data);
      } catch (error) {
        console.log(error);
      }
    };

      fetchGroups();
    }, []);

  return (
    <>
      <Row className="justify-content-center">
        <Col xs={12} md={8} lg={6}>
          <h1>My Groups</h1>
        </Col>
        <Col xs={12} md={4} lg={6} className="d-flex justify-content-end">
          <Button variant="primary" href="/groups/create">Create Group</Button>
        </Col>
      </Row>
      
      <div className="d-flex justify-content-center">
        <ListGroup className="w-100">
          {userGroups.map((group) => (
            <ListGroup.Item key={group.id} className="border-0">
              <GroupUserInfo group={group} />
            </ListGroup.Item>
          ))}
        </ListGroup>
      </div>
    </>
  )
}

export default MyGroupsScreen