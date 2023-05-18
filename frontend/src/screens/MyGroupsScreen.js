import React, { useContext } from 'react'
import { useState, useEffect } from "react";
import axios from "../axios";

import ListGroup from "react-bootstrap/ListGroup";
import GroupUserInfo from '../components/GroupUserInfo';
import { AuthContext } from '../AuthContext';
import { Button, Col, Row, ButtonGroup, FormControl } from 'react-bootstrap';
import { Link } from 'react-router-dom';

const MyGroupsScreen = () => {

  const [userGroups, setUserGroups] = useState([]);
  const userInfo = useContext(AuthContext).userInfo;

  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const size = 5; 

  useEffect(() => {

    console.log(userInfo);

    const fetchGroups = async () => {
      try {
        const { data } = await axios.get(`/api/v1/groups/user/${userInfo.userId}?page=${page}&size=${size}`);
      
        setTotalPages(data.totalPages);
        setUserGroups(data.content);
      } catch (error) {
        console.log(error);
      }
    };

      fetchGroups();
    }, [page, size]);
  
  const handleNextPage = () => {
    if (page < totalPages - 1) {
      setPage(prevPage => prevPage + 1);
    }
  };

  const handlePreviousPage = () => {
    if (page > 0) {
      setPage(prevPage => prevPage - 1);
    }
  };

  const handlePageChange = (event) => {
    const newPage = Number(event.target.value);
    if (newPage >= 0 && newPage < totalPages) {
      setPage(newPage);
    }
  };

  return (
    <>
      <Row className="justify-content-center">
        <Col xs={12} md={8} lg={6}>
          <h1>My Groups</h1>
        </Col>
        <Col xs={12} md={4} lg={6} className="d-flex justify-content-end">
          <Link className="btn btn-secondary my-3" to="/groups/create">
          Create Group
        </Link>
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

      <div className="d-flex justify-content-center">
        <ButtonGroup>
          <Button onClick={handlePreviousPage} disabled={page === 0}>
            Previous
          </Button>
          <FormControl
            type="number"
            value={page+1}
            onChange={handlePageChange}
            style={{ width: '100px' }}
          />
          <Button onClick={handleNextPage} disabled={page === totalPages - 1}>
            Next
          </Button>
        </ButtonGroup>
      </div>
    </>
  )
}

export default MyGroupsScreen